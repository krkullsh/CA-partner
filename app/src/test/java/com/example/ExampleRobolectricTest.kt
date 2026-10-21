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
  fun `verify offline lab engine generates structured verdict`() {
    val topic = CaSyllabusData.topics.first()
    val explanation = "Under AS 4, adjusting events provide additional information of conditions existing on the balance sheet date. Going concern is always an adjusting event."
    val verdict = OfflineLabEngine.generateVerdict(topic, explanation)

    assertNotNull(verdict)
    assertTrue(verdict.masteryScore in 50..100)
    assertTrue(verdict.whatYouNailed.isNotEmpty())
    assertTrue(verdict.icaiExaminerTip.isNotBlank())
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
}
