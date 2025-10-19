
## 架構圖與資料流（UI → ViewModel → UseCase → Repository → Remote/Local
#### 系統設定 MyApplication - 內含Timber初始化
#### 入口點 MyActivity 內為TabLayout+ViewPager2 共兩個Tab

##### 第一個tab為Pokemon清單 UI為PokemonListFragment

	資料來源為PokemonAPI
	
	RetrofitManager的PokemonService - PokemonRepoImpl(由Paging 3控制分頁資料存取) - PokemonListViewModel - 顯示在PokemonListFragment
	
	點擊單一項目後 進入Detail

	RetrofitManager的PokemonService - PokemonRepoImpl - PokemonDetailViewModel - 顯示在PokemonDetailFragment並存入local room資料庫

#### 第二個tab為Resume清單 UI為ResumeListFragment

	資料來源為Local Room資料庫
	
	MyDatabase的ResumeDao(由Paging 3控制分頁資料存取) - ResumeRepo - ResumeViewModel - ResumeListFragment

## 分頁策略（limit/offset 或 page）

    依照PokemonAPI採用limit與offset控制

## 測試方式與案例說明

    單元測試 EntityTester.kt - 簡單測試Entity中有的函數
    實體元件測試 MyDatabaseTester.kt - 以記憶體模擬的方式測試Room資料庫

## 截圖
![pokemon_list]("./pokemon_list.png")
![pokemon_detail]("./pokemon_detail.png")
![resume_list]("./pokemon_resume_list.png")
