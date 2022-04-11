### csvProject
Araku system

### 2022.04.11 JAIKO管理システムにて仕様変更
- 1．注文状況画面→新規登録　ボタン押下時、何も検索しないように変更する。
- 2．新規登録ボタン押下時、SUB画面の幅と高さをもっと大きくする。
- 3．SUB画面の詳細のカラムを以下の4項目に変更する。
 ①商品名検索
 ②商品名
 ③数量(箱)
 ④数量(個)

### 2022.02.03 라쿠텐 order_no 길이변경 대응
- alter table rakuten_frozen_info modify column order_no VARCHAR(30); // 라쿠텐 냉동
- alter table rakuten_info modify column order_no VARCHAR(30); // 라쿠텐
- alter table prd_trans_info modify column order_no VARCHAR(30); // 중간마스터

### 見積書
- init.sql 최하단 esitmate_info 테이블 추가
![image](https://user-images.githubusercontent.com/18359068/150670405-74d60872-c6cd-4098-94e3-8709c7bd77da.png)
- 화면 확인
![image](https://user-images.githubusercontent.com/18359068/150670441-5b813687-9484-4804-81a3-fcb95ed31ca1.png)
![image](https://user-images.githubusercontent.com/18359068/150670454-13388c79-18a6-419e-9a5f-8f1ec4b40418.png)
![image](https://user-images.githubusercontent.com/18359068/150670467-22bf395a-9603-45f2-a905-ae8609483804.png)
![image](https://user-images.githubusercontent.com/18359068/150670486-fdad38c7-b917-4bf9-9041-54d876332dc8.png)
![image](https://user-images.githubusercontent.com/18359068/150670503-7cc569f1-b4e4-4431-a14f-d325c60d8ead.png)
 - 작성 > 일괄 변경시
![image](https://user-images.githubusercontent.com/18359068/150670517-ae3e0b90-8a50-4e4b-a38e-9cbd5f551bfb.png)
- 개별 변경시
![image](https://user-images.githubusercontent.com/18359068/150670550-caefdeba-8aba-4cae-9e27-6d69382f1f25.png)
![image](https://user-images.githubusercontent.com/18359068/150670560-5f57636c-10b8-4f2a-978f-65fc1ebcf044.png)
- 다운로드
![image](https://user-images.githubusercontent.com/18359068/150670580-7300c431-5ac1-4d6c-97cc-4ba9f12063ce.png)
