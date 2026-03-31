# DJTimer

## 概要

DJTimerは、DJ用のタイマーアプリです。
DJ開始前に手番の開始時間と終了時間を設定しておくと、手番の開始までをカウントダウンして5分前に通知してくれます。
また手番中は、終了時間までを見やすい形でカウントダウンしてくれます。

手番開始時間や終了時間を入力しなくても、DJのプレイ時間を設定するだけですぐにカウントを始めてくれる機能もあります。

本アプリは**縦型折りたたみスマートフォン**に最適化されていて、画面の折り目に配慮したレイアウトになっています。
もちろん通常のスマートフォンでも問題なく使えます。

## 対応バージョン

- **最小動作バージョン**: Android 8.0 (API 26)
- **折りたたみデバイス機能**: Android 12 (API 31) 以上

折りたたみスマートフォンの画面分割機能を使う場合は、Android 12以上が必要になります。

## 使用技術

- **言語**: Kotlin
- **UIフレームワーク**: Jetpack Compose
- **アーキテクチャ**: MVVM + Clean Architecture
- **DIフレームワーク**: Dagger Hilt
- **非同期処理**: Kotlin Coroutines / Flow
- **ナビゲーション**: Jetpack Navigation Compose
- **折りたたみデバイス対応**: Jetpack WindowManager

## インストール方法

本アプリはGoogle Playストアには公開していません。
以下の手順でインストールしてください。

1. GitHubの[Releases](https://github.com/playjp-ott-dev-3g/DJTimer/releases)ページにアクセス
2. 最新のリリースから `.apk` ファイルをダウンロード
3. ダウンロードしたAPKファイルをタップしてインストール

※ 初回インストール時は、Android端末の設定で「提供元不明のアプリのインストール」を許可する必要があります。

## オリジナル

このアプリは、[Gigandect](https://github.com/Gigandect)さんが開発したWeb版の[DJTimer](https://gigandect.github.io/DJtimer/)を参考にAndroidアプリとして再実装したものです。
オリジナルのWeb版もぜひご利用ください。

## 制作者
アプリの不具合等は以下のアカウントまでお問い合わせください。

**あかしん**

映像を作ったりイラストを描いたりしています。たまにDJも。

- X (Twitter): [@aksn_1](http://x.com/aksn_1)
- Instagram: [@1aksn](https://instagram.com/1aksn)

## ライセンス

本プロジェクトのライセンスについては、リポジトリのLICENSEファイルをご確認ください。
