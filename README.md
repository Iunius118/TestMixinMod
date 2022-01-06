# TestMixinMod

Testing mixin mod of Forge 1.18.1

## Description

プレイヤーがアイテムフレーム内のアイテムを右クリックして回転させようとしたときに発生するイベント`RotateItemInItemFrameEvent`を作成し、Mixinを利用して実際にそのイベントが発生するようにする。

- [Forge modding環境へのMixinの導入](https://github.com/SpongePowered/Mixin/wiki/Mixins-on-Minecraft-Forge)
- [Mixin config jsonの設定項目](https://github.com/SpongePowered/Mixin/wiki/Introduction-to-Mixins---The-Mixin-Environment#mixin-configuration-files)
- [Callback Injectors（`@Inject`）の使用方法](https://github.com/SpongePowered/Mixin/wiki/Advanced-Mixin-Usage---Callback-Injectors)

### com.example.testmixinmod.TestMixinMod

Modのコアクラス。作成したイベントが発生した際に呼び出されるリスナーメソッドを含む。

### com.example.testmixinmod.TestEventHook

イベントのクラス。イベントを発生させるためのメソッドと発生するイベント`RotateItemInItemFrameEvent`クラスを含む。

### com.example.testmixinmod.mixin.MixinItemFrame

Mixinを使用するクラス。`RotateItemInItemFrameEvent`イベントを発生させ、またイベントがキャンセルされたときの処理をするために`ItemFrame.interact`に注入されるメソッドを含む。

イベント発生に関する処理は以下のようになる。

1. プレイヤーがアイテムフレーム内のアイテムを右クリック
2. `ItemFrame.interact`
3. `MixinItemFrame.fireRotateItemInItemFrameEvent`
4. `TestEventHook.onRotatingItemInItemFrame`（`TestEventHook.RotateItemInItemFrameEvent`を発生）
5. Forge event bus
6. `TestMixinMod.onRotatingItemInItemFrame`（イベントリスナー）
7. `MixinItemFrame.fireRotateItemInItemFrameEvent`
   - イベントがキャンセルされなかった場合は`ItemFrame.interact`の処理を続行し、アイテムフレーム内のアイテムが回転する
   - イベントがキャンセルされた場合は`ItemFrame.interact`の処理を中断し、アイテムフレーム内のアイテムは回転しない

なお、今回は面白そうなのでForgeのイベントシステムを利用したが、作成したイベントをプラグインや他modに公開するつもりがない場合にはイベントにする必要はない。
