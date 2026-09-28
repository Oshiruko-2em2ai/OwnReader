# OwnReader - Android Comic Reader App

Android用の個人専用コミックリーダーアプリです。Kotlin + Jetpack Composeで実装されています。

## 機能

- コミック/漫画の閲覧
- 複数フォーマット対応（JPG、PNG、WebP）
- ZIP圧縮ファイルの自動解凍
- 閲覧履歴の記録
- ブックマーク機能
- ダークモード対応

## 技術スタック

- **言語**: Kotlin
- **UI フレームワーク**: Jetpack Compose (Material3)
- **Database**: Room (SQLite)
- **非同期処理**: Coroutines
- **依存性注入**: Hilt
- **ナビゲーション**: Navigation Compose
- **画像読み込み**: Coil
- **ファイル圧縮**: Zip4j
- **ログ**: Timber

## プロジェクト構造

```
app/
├── src/main/
│   ├── kotlin/com/ownreader/
│   │   ├── ui/
│   │   │   ├── theme/              # Material3 テーマ
│   │   │   ├── screens/            # 画面コンポーネント
│   │   │   └── navigation/         # ナビゲーション
│   │   ├── data/
│   │   │   ├── database/           # Room Database
│   │   │   ├── model/              # データモデル
│   │   │   ├── repository/         # リポジトリ層
│   │   │   └── di/                 # Dagger Hilt モジュール
│   │   ├── MainActivity.kt
│   │   └── OwnReaderApplication.kt
│   └── res/
│       ├── values/                 # リソース値
│       └── xml/                    # XML リソース
└── build.gradle.kts
```

## セットアップ

### 必要な環境
- Android Studio 2023.2以上
- Android SDK API 34
- JDK 17

### ビルド方法

1. プロジェクトをクローン
```bash
git clone https://github.com/Oshiruko-2em2ai/OwnReader.git
```

2. Android Studioで開く

3. ビルド
```bash
./gradlew build
```

4. エミュレータ/実機で実行
```bash
./gradlew installDebug
```

## ロードマップ

### Phase 1 (MVP)
- [ ] コミック画像フォルダの読み込み
- [ ] 基本的な閲覧機能（ページ送り、ズーム）
- [ ] ライブラリ表示

### Phase 2
- [ ] 閲覧履歴保存
- [ ] ブックマーク機能
- [ ] 検索・ソート機能

### Phase 3
- [ ] テーマカスタマイズ
- [ ] 高度なフィルタリング
- [ ] パフォーマンス最適化

## ライセンス

プライベートプロジェクト

## 作成者

Oshiruko-2em2ai
