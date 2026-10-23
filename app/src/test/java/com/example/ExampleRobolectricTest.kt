package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ai.OfflineLabEngine
import com.example.data.curriculum.CaSyllabusData
import com.example.data.curriculum.CuratedCatchLabScenarios
import com.example.data.curriculum.CuratedDifferences
import com.example.data.model.PaperCode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("CA-Lab", appName)
  }

  @Test
  fun `verify syllabus covers all 6 ICAI papers`() {
    val allPapers = PaperCode.entries
    assertEquals(6, allPapers.size)

    allPapers.forEach { paper ->
      val topics = CaSyllabusData.getTopicsForPaper(paper)
      assertTrue("Paper ${paper.code} should have syllabus topics", topics.isNotEmpty())
    }
  }

  @Test
  fun `verify curated catch lab scenarios have valid traps`() {
    val scenarios = CuratedCatchLabScenarios.scenarios
    assertTrue(scenarios.isNotEmpty())
    scenarios.forEach { scenario ->
      assertTrue(scenario.subtleErrors.isNotEmpty())
      assertEquals(scenario.errorCount, scenario.subtleErrors.size)
      assertTrue(scenario.correctIcaiProvision.isNotBlank())
    }
  }

  @Test
  fun `verify curated concept differences have memory hooks`() {
    val diffs = CuratedDifferences.differences
    assertTrue(diffs.isNotEmpty())
    diffs.forEach { diff ->
      assertTrue(diff.comparisonPoints.isNotEmpty())
      assertTrue(diff.memoryHook.isNotBlank())
    }
  }

  @Test
  fun `verify Paper 1 and Paper 5 have complete syllabus with StandardBlueprints`() {
    val paper1Topics = CaSyllabusData.getTopicsForPaper(PaperCode.PAPER_1)
    val paper5Topics = CaSyllabusData.getTopicsForPaper(PaperCode.PAPER_5)

    // Paper 1 checks (26 AS + Company Accounts & Modules)
    assertTrue("Paper 1 should have at least 26 standards & chapters", paper1Topics.size >= 26)
    val paper1Blueprints = paper1Topics.filter { it.blueprint != null }
    assertTrue("All Paper 1 topics should have non-null StandardBlueprint", paper1Blueprints.size >= 26)

    // Paper 5 checks (11 ICAI chapters & SAs)
    assertEquals("Paper 5 should have 11 comprehensive chapters", 11, paper5Topics.size)
    val paper5Blueprints = paper5Topics.filter { it.blueprint != null }
    assertEquals("All 11 Paper 5 chapters should have non-null StandardBlueprint", 11, paper5Blueprints.size)

    // Verify Blueprint fields
    paper1Topics.forEach { t ->
      t.blueprint?.let { bp ->
        assertTrue(bp.scope.isNotBlank())
        assertTrue(bp.recognitionAndMeasurement.isNotBlank())
        assertTrue(bp.disclosure.isNotBlank())
      }
    }
  }

  @Test
  fun `verify Papers 2, 3, 4, and 6 have complete curriculum coverage with key provisions`() {
    val p2 = CaSyllabusData.getTopicsForPaper(PaperCode.PAPER_2)
    val p3 = CaSyllabusData.getTopicsForPaper(PaperCode.PAPER_3)
    val p4 = CaSyllabusData.getTopicsForPaper(PaperCode.PAPER_4)
    val p6 = CaSyllabusData.getTopicsForPaper(PaperCode.PAPER_6)

    assertTrue("Paper 2 should have at least 15 chapters", p2.size >= 15)
    assertTrue("Paper 3 should have at least 27 chapters", p3.size >= 27)
    assertTrue("Paper 4 should have at least 15 chapters", p4.size >= 15)
    assertTrue("Paper 6 should have at least 14 chapters", p6.size >= 14)

    CaSyllabusData.topics.forEach { topic ->
      assertTrue("${topic.id} in ${topic.paper} must have at least 3 key provisions", topic.keyProvisions.size >= 3)
      assertTrue("${topic.id} must have non-blank examiner pitfalls", topic.commonExaminerPitfalls.isNotBlank())
      assertTrue("${topic.id} must have non-blank moduleName", topic.moduleName.isNotBlank())
    }
  }

  @Test
  fun `verify offline lab engine generates structured verdict`() {
    val topic = CaSyllabusData.topics.first { it.id == "p1_as4" }
    val explanation = "Under AS 4, adjusting events provide additional information of conditions existing on the balance sheet date. Going concern is always an adjusting event."
    val verdict = OfflineLabEngine.generateVerdict(topic, explanation)

    assertNotNull(verdict)
    assertTrue(verdict.masteryScore in 50..100)
    assertTrue(verdict.whatYouNailed.isNotEmpty())
    assertTrue(verdict.icaiExaminerTip.isNotBlank())
  }

  @Test
  fun `verify offline lab engine generates topic-aware follow-up doubt`() {
    val topic = CaSyllabusData.topics.first { it.id == "p2_sec135" } // CSR
    val doubt = OfflineLabEngine.generateFollowUpDoubt(topic, "CSR is mandatory for companies", 1)
    assertTrue(doubt.contains(topic.topicName))
  }

  @Test
  fun `verify on-demand database query executes without background flow`() = kotlinx.coroutines.test.runTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = com.example.data.local.CaLabDatabase.getInstance(context)
    val repo = com.example.data.repository.StudyRepository(db, context)

    val topic = CaSyllabusData.topics.first()
    val verdict = OfflineLabEngine.generateVerdict(topic, "Testing on-demand query")
    repo.saveVerdict(verdict)

    val list = repo.getVerdictsOnce()
    assertTrue(list.isNotEmpty())
    val avg = repo.getAverageMasteryScoreOnce()
    assertTrue(avg > 0)
    val count = repo.getTotalSessionsCountOnce()
    assertTrue(count >= 1)
  }

  @Test
  fun `verify markdown formatter parses bold and clean bullets`() {
    val input = "- This is **bold text** in CA Inter\n* Another line"
    val parsed = com.example.ui.components.parseMarkdownToAnnotatedString(input)
    assertTrue(parsed.text.contains("• This is bold text in CA Inter"))
    assertTrue(parsed.spanStyles.isNotEmpty())
  }

  @Test
  fun `verify bookmark toggle in repository`() = kotlinx.coroutines.test.runTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = com.example.data.local.CaLabDatabase.getInstance(context)
    val repo = com.example.data.repository.StudyRepository(db, context)

    val topicId = "test_topic_123"
    repo.toggleBookmark(topicId, "TOPIC", "Test Topic", "Paper 1")
    assertTrue(repo.isBookmarked(topicId))

    repo.toggleBookmark(topicId, "TOPIC", "Test Topic", "Paper 1")
    org.junit.Assert.assertFalse(repo.isBookmarked(topicId))
  }

  @Test
  fun `verify MarkdownQuestionParser and CuratedQuestionBank provide high-chance questions`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val allPapers = PaperCode.entries

    allPapers.forEach { paper ->
      val questions = com.example.data.curriculum.MarkdownQuestionParser.getQuestionsForPaper(context, paper)
      assertTrue("Paper ${paper.code} should have practice questions", questions.isNotEmpty())

      questions.forEach { q ->
        assertTrue("Question ${q.id} should have valid marks", q.marks > 0)
        assertTrue("Question ${q.id} should have valid minutes", q.minutesAllowed > 0)
        assertTrue("Question ${q.id} should have non-blank official answer", q.officialAnswer.isNotBlank())
        assertTrue("Question ${q.id} should have non-blank approach tips", q.approachTips.isNotBlank())
        assertTrue("Question ${q.id} should have non-blank examiner observations", q.examinerObservations.isNotBlank())
      }
    }

    // Verify Compulsory Q1 Fused questions exist
    val p1Questions = com.example.data.curriculum.MarkdownQuestionParser.getQuestionsForPaper(context, PaperCode.PAPER_1)
    assertTrue(p1Questions.any { it.id == "p1_q1_fused_as15_as10" })

    val p2Questions = com.example.data.curriculum.MarkdownQuestionParser.getQuestionsForPaper(context, PaperCode.PAPER_2)
    assertTrue(p2Questions.any { it.id == "p2_q1_fused_law" })
  }

  @Test
  fun `verify TopicProgress tracking in database and repository`() = kotlinx.coroutines.test.runTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = com.example.data.local.CaLabDatabase.getInstance(context)
    val repo = com.example.data.repository.StudyRepository(db, context)

    val topicId = "p1_as10"
    repo.recordWrittenPractice(topicId)
    repo.recordWrittenPractice(topicId)
    repo.recordOralRecall(topicId)
    repo.recordMcqPractice(topicId, attempted = 10, correct = 8)

    val progress = repo.getTopicProgress(topicId)
    assertNotNull(progress)
    assertEquals(2, progress?.writtenSolved)
    assertEquals(1, progress?.oralRecallsCompleted)
    assertEquals(10, progress?.mcqAttempted)
    assertEquals(8, progress?.mcqCorrect)
    assertTrue((progress?.lastRevisedAt ?: 0L) > 0L)
  }

  @Test
  fun `verify StrategyBlueprintReader loads markdown for all 6 papers`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = com.example.data.local.CaLabDatabase.getInstance(context)
    val repo = com.example.data.repository.StudyRepository(db, context)

    PaperCode.entries.forEach { paper ->
      val md = repo.getStrategyBlueprintMarkdown(paper)
      assertTrue("Blueprint for ${paper.name} should not be blank", md.isNotBlank())
      assertTrue("Blueprint for ${paper.name} should contain strategy keywords", md.contains("Strategy", ignoreCase = true) || md.contains("Blueprint", ignoreCase = true) || md.contains("Section", ignoreCase = true))
    }
  }
}

