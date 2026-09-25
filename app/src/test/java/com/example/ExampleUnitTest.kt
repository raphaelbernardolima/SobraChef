package com.example

import com.example.data.remote.RecipeCatalog
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testRecipeCatalogMatchesLeftovers() {
    val leftovers = listOf("Arroz cozido", "Ovos", "Tomate")
    val matches = RecipeCatalog.matchRecipes(leftovers)
    assertTrue("Deveria encontrar receitas para sobras de arroz e ovos", matches.isNotEmpty())
    val first = matches.first()
    assertTrue(first.wasteScore >= 80)
  }
}
