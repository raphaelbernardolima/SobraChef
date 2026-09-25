package com.example.data.remote

import com.example.data.model.Recipe
import com.example.data.model.RecipeIngredient

object RecipeCatalog {
    val defaultRecipes: List<Recipe> = listOf(
        Recipe(
            id = 1,
            title = "Arroz de Forno Cremoso da Geladeira",
            description = "O clássico salvador de sobras! Transforma arroz amanhecido em uma refeição cremosa, gratinada e reconfortante.",
            prepTimeMinutes = 25,
            difficulty = "Fácil",
            baseServings = 4,
            wasteScore = 96,
            costPerServing = 4.20,
            savingsEstimate = 28.50,
            ingredients = listOf(
                RecipeIngredient("Arroz cozido", 3.0, "xícaras", isAvailable = true),
                RecipeIngredient("Ovos", 2.0, "unidades", isAvailable = true),
                RecipeIngredient("Queijo mussarela ou prato", 150.0, "g", isAvailable = true),
                RecipeIngredient("Cenoura ralada", 1.0, "unidade", isAvailable = true),
                RecipeIngredient("Tomate picado", 1.0, "unidade", isAvailable = true),
                RecipeIngredient("Leite", 1.0, "xícara", isAvailable = false, isPantryItem = true),
                RecipeIngredient("Sobras de frango ou presunto", 150.0, "g", isAvailable = true),
                RecipeIngredient("Cheiro-verde", 2.0, "colheres de sopa", isAvailable = true)
            ),
            instructions = listOf(
                "Em uma tigela grande, bata ligeiramente os 2 ovos com o leite, sal e pimenta.",
                "Adicione o arroz amanhecido, a cenoura ralada, o tomate e as sobras de carnes ou frios.",
                "Misture bem até obter uma consistência úmida e homogênea.",
                "Transfira para um refratário untado, cubra com o queijo e orégano a gosto.",
                "Leve ao forno pré-aquecido a 200°C por 20 minutos até dourar e borbulhar.",
                "Sirva quente e aproveite a cremosidade sem desperdiçar nenhum grão!"
            ),
            missingItems = listOf("Leite"),
            tips = "Se não tiver leite, use creme de leite, requeijão ou um pouco de caldo de legumes quente."
        ),
        Recipe(
            id = 2,
            title = "Frittata Rica de Legumes da Gaveta",
            description = "Aproveita todos aqueles pedaços de legumes esquecidos no fundo da geladeira em uma omelete alta italiana fofinha.",
            prepTimeMinutes = 15,
            difficulty = "Fácil",
            baseServings = 2,
            wasteScore = 98,
            costPerServing = 3.10,
            savingsEstimate = 21.00,
            ingredients = listOf(
                RecipeIngredient("Ovos", 4.0, "unidades", isAvailable = true),
                RecipeIngredient("Cebola picada", 1.0, "unidade", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Tomate", 1.0, "unidade", isAvailable = true),
                RecipeIngredient("Abobrinha ou chuchu", 1.0, "unidade", isAvailable = true),
                RecipeIngredient("Queijo ralado ou sobras de queijo", 50.0, "g", isAvailable = true),
                RecipeIngredient("Azeite ou manteiga", 1.0, "colher de sopa", isAvailable = false, isPantryItem = true),
                RecipeIngredient("Sal e pimenta", 1.0, "pitada", isAvailable = true, isPantryItem = true)
            ),
            instructions = listOf(
                "Em uma frigideira antiaderente média, aqueça o azeite e refogue a cebola até dourar.",
                "Acrescente a abobrinha em cubos e o tomate picado. Refogue por 3 minutos em fogo médio.",
                "Bata os 4 ovos com um garfo, tempere com sal, pimenta e metade do queijo.",
                "Despeje os ovos sobre os legumes na frigideira, tampe e cozinhe em fogo baixo por 7 minutos.",
                "Polvilhe o restante do queijo por cima, tampe por mais 2 minutos até firmar o centro.",
                "Deslize para um prato e sirva com salada de folhas ou pão amanhecido tostado."
            ),
            missingItems = listOf("Azeite"),
            tips = "Talos de espinafre, brócolis ou folhas de beterraba ficam deliciosos fatiados fininhos nesta receita."
        ),
        Recipe(
            id = 3,
            title = "Mexidão Mineiro Especial Zero Desperdício",
            description = "O campeão de sabor e economia da culinária brasileira. Transforma arroz, feijão e sobras de carne em um banquete.",
            prepTimeMinutes = 20,
            difficulty = "Fácil",
            baseServings = 3,
            wasteScore = 100,
            costPerServing = 3.50,
            savingsEstimate = 26.00,
            ingredients = listOf(
                RecipeIngredient("Arroz cozido", 2.0, "xícaras", isAvailable = true),
                RecipeIngredient("Feijão cozido (com ou sem caldo)", 1.5, "xícaras", isAvailable = true),
                RecipeIngredient("Ovos", 2.0, "unidades", isAvailable = true),
                RecipeIngredient("Couve ou repolho fatiado", 1.0, "xícara", isAvailable = true),
                RecipeIngredient("Cebola e alho picados", 2.0, "colheres de sopa", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Sobras de carne, frango ou linguiça", 120.0, "g", isAvailable = true),
                RecipeIngredient("Farinha de mandioca ou milho", 2.0, "colheres de sopa", isAvailable = true, isPantryItem = true)
            ),
            instructions = listOf(
                "Aqueça uma panela grande com um fio de óleo ou azeite.",
                "Frite o alho e a cebola até dourarem perfumando a cozinha.",
                "Junte as sobras de carnes e refogue por 2 minutos.",
                "Afaste os ingredientes para o lado da panela e quebre os ovos no espaço livre, mexendo até cozinharem.",
                "Adicione o arroz, o feijão e a couve fatiada fininha. Mexa delicadamente para envolver tudo.",
                "Finalize salpicando a farinha de mandioca e cheiro-verde. Sirva fumegante!"
            ),
            missingItems = emptyList(),
            tips = "Pode colocar umas gotinhas de limão e molho de pimenta na hora de servir para abrir os sabores."
        ),
        Recipe(
            id = 4,
            title = "Torta Rápida de Liquidificador 'Limpa Despensa'",
            description = "Massa fofinha feita em 3 minutos no liquidificador com ingredientes básicos de dispensa, recheada com o que você tiver.",
            prepTimeMinutes = 35,
            difficulty = "Médio",
            baseServings = 6,
            wasteScore = 92,
            costPerServing = 4.80,
            savingsEstimate = 34.00,
            ingredients = listOf(
                RecipeIngredient("Farinha de trigo", 2.0, "xícaras", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Leite", 1.5, "xícaras", isAvailable = false, isPantryItem = true),
                RecipeIngredient("Óleo", 0.5, "xícara", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Ovos", 3.0, "unidades", isAvailable = true),
                RecipeIngredient("Fermento em pó", 1.0, "colher de sopa", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Sobras de frango desfiado ou atum", 200.0, "g", isAvailable = true),
                RecipeIngredient("Tomate e cebola picados", 1.0, "xícara", isAvailable = true),
                RecipeIngredient("Milho verde ou ervilha", 0.5, "lata", isAvailable = true, isPantryItem = true)
            ),
            instructions = listOf(
                "No liquidificador, bata os ovos, o leite, o óleo e uma pitada de sal por 1 minuto.",
                "Acrescente a farinha de trigo aos poucos e bata até formar uma massa lisa.",
                "Por último, adicione o fermento e bata na tecla pulsar apenas para misturar.",
                "Despeje metade da massa em forma untada.",
                "Espalhe o recheio de sobras de frango, milho, tomate e temperos.",
                "Cubra com o restante da massa e asse a 180°C por 30 a 35 minutos até dourar."
            ),
            missingItems = listOf("Leite"),
            tips = "Excelente para congelar em fatias e levar como marmita no trabalho durante a semana."
        ),
        Recipe(
            id = 5,
            title = "Bolinho Crocante de Arroz e Ervas",
            description = "O petisco mais econômico do mundo! Casca crocante por fora e interior cremoso que agrada toda a família.",
            prepTimeMinutes = 20,
            difficulty = "Fácil",
            baseServings = 4,
            wasteScore = 95,
            costPerServing = 2.40,
            savingsEstimate = 18.00,
            ingredients = listOf(
                RecipeIngredient("Arroz cozido amanhecido", 2.0, "xícaras", isAvailable = true),
                RecipeIngredient("Ovo", 1.0, "unidade", isAvailable = true),
                RecipeIngredient("Farinha de trigo", 0.5, "xícara", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Queijo ralado", 3.0, "colheres de sopa", isAvailable = true),
                RecipeIngredient("Cebolinha e salsinha picadas", 2.0, "colheres de sopa", isAvailable = true),
                RecipeIngredient("Fermento em pó", 1.0, "colher de chá", isAvailable = true, isPantryItem = true)
            ),
            instructions = listOf(
                "Em uma tigela, amasse levemente metade do arroz com um garfo para dar liga.",
                "Junte o ovo batido, o queijo ralado, o cheiro-verde, sal e pimenta.",
                "Acrescente a farinha e o fermento, misturando até dar consistência de modelar com duas colheres.",
                "Frite em óleo quente até dourar ou asse na Airfryer a 190°C por 12 minutos borrifando azeite.",
                "Escorra em papel toalha e sirva com molho de tomate caseiro."
            ),
            missingItems = emptyList(),
            tips = "Se tiver pedacinhos de queijo ou presunto sobrando, coloque um cubinho no centro de cada bolinho!"
        ),
        Recipe(
            id = 6,
            title = "Macarrão Caçarola Cremoso da Geladeira",
            description = "Massa aveludada em uma única panela, unindo queijos, embutidos e legumes que estavam esquecidos.",
            prepTimeMinutes = 18,
            difficulty = "Fácil",
            baseServings = 3,
            wasteScore = 89,
            costPerServing = 4.10,
            savingsEstimate = 22.00,
            ingredients = listOf(
                RecipeIngredient("Macarrão (qualquer tipo)", 250.0, "g", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Creme de leite ou requeijão", 1.0, "caixinha", isAvailable = false),
                RecipeIngredient("Sobras de queijo", 100.0, "g", isAvailable = true),
                RecipeIngredient("Tomate picado", 1.0, "unidade", isAvailable = true),
                RecipeIngredient("Alho e cebola picados", 2.0, "colheres de sopa", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Cenoura ou abobrinha ralada", 0.5, "unidade", isAvailable = true)
            ),
            instructions = listOf(
                "Cozinhe o macarrão em água fervente com sal até ficar al dente. Reserve meia xícara da água do cozimento.",
                "Na mesma panela, doure o alho e a cebola no azeite.",
                "Adicione a cenoura ralada e o tomate picado, refogando por 2 minutos.",
                "Junte o creme de leite, a água do cozimento reservada e as sobras de queijo.",
                "Misture o macarrão de volta na panela em fogo brando até o queijo derreter completamente.",
                "Finalize com orégano e sirva imediatamente cremoso e perfumado."
            ),
            missingItems = listOf("Creme de leite"),
            tips = "A água do cozimento do macarrão é rica em amido e deixa o molho incrivelmente aveludado sem grumos."
        ),
        Recipe(
            id = 7,
            title = "Sopa Reconfortante de Talos e Cascas Ricas",
            description = "Aproveitamento 100% integral dos alimentos. Nutrição concentrada com cascas higienizadas e legumes esquecidos.",
            prepTimeMinutes = 28,
            difficulty = "Fácil",
            baseServings = 4,
            wasteScore = 100,
            costPerServing = 2.80,
            savingsEstimate = 27.00,
            ingredients = listOf(
                RecipeIngredient("Batatas com casca", 2.0, "unidades", isAvailable = true),
                RecipeIngredient("Cenoura com casca", 1.0, "unidade", isAvailable = true),
                RecipeIngredient("Talos de brócolis ou couve", 1.0, "xícara", isAvailable = true),
                RecipeIngredient("Cebola picada", 1.0, "unidade", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Alho amassado", 2.0, "dentes", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Azeite ou óleo", 1.0, "colher de sopa", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Água ou caldo caseiro", 4.0, "xícaras", isAvailable = true, isPantryItem = true)
            ),
            instructions = listOf(
                "Higienize bem os legumes com uma escovinha sob água corrente.",
                "Pique as batatas, cenouras e os talos em pedaços médios.",
                "Em panela média, refogue o alho e a cebola no azeite até dourarem.",
                "Acrescente todos os legumes e talos, cobrindo com a água e sal a gosto.",
                "Cozinhe por 20 minutos até que os legumes estejam bem macios.",
                "Bata metade no liquidificador para dar textura aveludada e junte ao restante da sopa.",
                "Sirva com um fio de azeite e torradas de pão amanhecido."
            ),
            missingItems = emptyList(),
            tips = "As cascas de vegetais bem lavadas concentram antioxidantes e dão uma cor dourada vibrante ao caldo."
        ),
        Recipe(
            id = 8,
            title = "Farofa Crocante 'Dispensa Inteligente'",
            description = "Prato coringa brasileiro que complementa qualquer almoço e aproveita pontas de cebola, ovos e farinhas.",
            prepTimeMinutes = 12,
            difficulty = "Fácil",
            baseServings = 4,
            wasteScore = 97,
            costPerServing = 2.20,
            savingsEstimate = 16.50,
            ingredients = listOf(
                RecipeIngredient("Farinha de mandioca ou milho", 2.0, "xícaras", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Ovos", 2.0, "unidades", isAvailable = true),
                RecipeIngredient("Manteiga ou óleo", 2.0, "colheres de sopa", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Cebola em rodelas finas", 1.0, "unidade", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Cenoura ralada", 0.5, "unidade", isAvailable = true),
                RecipeIngredient("Alho picadinho", 2.0, "dentes", isAvailable = true, isPantryItem = true)
            ),
            instructions = listOf(
                "Em frigideira grande, derreta a manteiga e doure o alho e a cebola lentamente.",
                "Junte a cenoura ralada e mexa por 1 minuto para soltar cor.",
                "Abra um espaço no centro e quebre os 2 ovos, mexendo até formarem pedaços suculentos.",
                "Adicione a farinha de mandioca aos poucos, mexendo sempre em fogo baixo para tostar por igual.",
                "Tempere com sal a gosto e retire do fogo quando a farofa estiver bem sequinha e crocante."
            ),
            missingItems = emptyList(),
            tips = "Pode guardar em pote de vidro bem fechado por até 5 dias sem perder a crocância."
        ),
        Recipe(
            id = 9,
            title = "Panquecas Recheadas com Sobras de Carnes",
            description = "Massa leve de crepe recheada com aquele restinho de carne de panela ou frango do almoço.",
            prepTimeMinutes = 24,
            difficulty = "Médio",
            baseServings = 3,
            wasteScore = 93,
            costPerServing = 4.30,
            savingsEstimate = 29.00,
            ingredients = listOf(
                RecipeIngredient("Leite", 1.0, "xícara", isAvailable = false, isPantryItem = true),
                RecipeIngredient("Ovo", 1.0, "unidade", isAvailable = true),
                RecipeIngredient("Farinha de trigo", 1.0, "xícara", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Sobras de carne ou frango desfiado", 200.0, "g", isAvailable = true),
                RecipeIngredient("Molho de tomate ou extrato", 0.5, "xícara", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Queijo ralado", 30.0, "g", isAvailable = true)
            ),
            instructions = listOf(
                "Bata no liquidificador o leite, ovo, farinha e uma pitada de sal até homogeneizar.",
                "Aqueça uma frigideira antiaderente untada com um pingo de óleo.",
                "Despeje uma concha pequena de massa, girando para cobrir o fundo finamente.",
                "Vire após 1 minuto para dourar o outro lado. Repita até acabar a massa.",
                "Aqueça as sobras de carne com um pouco do molho de tomate.",
                "Recheie cada disco de massa, enrole, cubra com o restante do molho e queijo ralado.",
                "Gratine por 5 minutos no forno ou micro-ondas e saboreie."
            ),
            missingItems = listOf("Leite"),
            tips = "Se a carne estiver seca, adicione umas colheradas de requeijão ou caldo para reidratar."
        ),
        Recipe(
            id = 10,
            title = "Banana Flambada Caramelizada com Casca",
            description = "Sobremesa gourmet pronta em 8 minutos usando aquelas bananas super maduras que iriam para o lixo.",
            prepTimeMinutes = 8,
            difficulty = "Fácil",
            baseServings = 2,
            wasteScore = 100,
            costPerServing = 1.90,
            savingsEstimate = 14.00,
            ingredients = listOf(
                RecipeIngredient("Bananas bem maduras", 2.0, "unidades", isAvailable = true),
                RecipeIngredient("Açúcar cristal ou mascavo", 2.0, "colheres de sopa", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Manteiga", 1.0, "colher de sopa", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Canela em pó", 1.0, "colher de chá", isAvailable = true, isPantryItem = true),
                RecipeIngredient("Água ou suco de laranja", 2.0, "colheres de sopa", isAvailable = true)
            ),
            instructions = listOf(
                "Corte as bananas ao meio no sentido do comprimento.",
                "Em frigideira média, derreta a manteiga com o açúcar até formar uma calda cor de caramelo claro.",
                "Acomode as fatias de banana com cuidado e deixe dourar por 2 minutos de cada lado.",
                "Pingue as colheres de água ou suco para soltar o caramelo e criar uma calda brilhante.",
                "Polvilhe bastante canela em pó por cima e sirva quente.",
                "Fica surreal com uma colherada de iogurte natural ou sorvete de creme."
            ),
            missingItems = emptyList(),
            tips = "A casca da banana bem lavada pode ser cortada em tirinhas e refogada com cebola como 'carne de casca de banana' salgada!"
        )
    )

    fun matchRecipes(
        availableIngredientNames: List<String>,
        maxTimeMinutes: Int? = null,
        difficultyFilter: String? = null,
        onlyComplete: Boolean = false
    ): List<Recipe> {
        val lowerAvailable = availableIngredientNames.map { it.trim().lowercase() }

        val scored = defaultRecipes.map { recipe ->
            var matchedCount = 0
            val updatedIngredients = recipe.ingredients.map { ing ->
                val isMatched = lowerAvailable.any { userIng ->
                    userIng.isNotEmpty() && (
                        ing.name.lowercase().contains(userIng) ||
                        userIng.contains(ing.name.lowercase()) ||
                        isSynonymOrCategory(userIng, ing.name.lowercase())
                    )
                }
                if (isMatched) matchedCount++
                ing.copy(isAvailable = isMatched)
            }

            val totalCount = recipe.ingredients.size
            val calculatedWasteScore = if (matchedCount > 0 && totalCount > 0) {
                // High waste savings score for recipes utilizing user's actual leftovers
                val pct = ((matchedCount.toDouble() / totalCount) * 100).toInt()
                minOf(100, maxOf(80, 75 + (pct * 25 / 100)))
            } else {
                recipe.wasteScore
            }

            val missingList = updatedIngredients.filter { !it.isAvailable }.map { it.name }

            recipe.copy(
                ingredients = updatedIngredients,
                missingItems = missingList,
                wasteScore = calculatedWasteScore
            )
        }

        return scored.filter { recipe ->
            val matchesTime = maxTimeMinutes == null || recipe.prepTimeMinutes <= maxTimeMinutes
            val matchesDifficulty = difficultyFilter == null || difficultyFilter == "Todos" || recipe.difficulty.equals(difficultyFilter, ignoreCase = true)
            val matchesComplete = !onlyComplete || recipe.missingItems.isEmpty()
            matchesTime && matchesDifficulty && matchesComplete
        }.sortedWith(
            compareByDescending<Recipe> { it.ingredients.count { ing -> ing.isAvailable } }
                .thenByDescending { it.wasteScore }
                .thenBy { it.missingItems.size }
        )
    }

    private fun isSynonymOrCategory(a: String, b: String): Boolean {
        val pairs = listOf(
            setOf("arroz", "arroz cozido", "arroz amanhecido"),
            setOf("ovo", "ovos", "clara", "gema"),
            setOf("queijo", "mussarela", "mozzarella", "prato", "parmesao", "parmesão"),
            setOf("frango", "frango desfiado", "frango cozido", "peito de frango"),
            setOf("carne", "carne moida", "carne moída", "carne de panela"),
            setOf("tomate", "tomates"),
            setOf("cenoura", "cenouras"),
            setOf("cebola", "cebolas"),
            setOf("leite", "creme de leite"),
            setOf("abobrinha", "legumes"),
            setOf("massa", "macarrao", "macarrão", "espaguete")
        )
        return pairs.any { pair -> pair.any { a.contains(it) } && pair.any { b.contains(it) } }
    }
}
