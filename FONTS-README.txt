Заміна шрифтів (Unbounded + Open Sans)
1. Розпакуйте архів у корінь проєкту (де settings.gradle.kts), погодьтеся на заміну Type.kt.
2. Видаліть старі шрифти (вони більше не використовуються):
   git rm shared/src/commonMain/composeResources/font/manrope_*.ttf shared/src/commonMain/composeResources/font/playfair_display_*.ttf
3. Gradle Sync і запуск.
