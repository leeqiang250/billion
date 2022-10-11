## 一、host
http://52.77.131.111:8890/

## 二、header
context: {"chain":"aptos", "language":"zh-TC"}

## 三、接口

###**config基础信息查询接口** 

| 方法 | GET |
|----|----|
| 路径 | /aptos/kiko/v1/config |

响应

`{
"code": 200,
"ts": 1664157505954,
"msg": "success",
"data": {
"currentContext": {
"chain": "aptos",
"language": "zh-TC"
},
"currentNode": {
"chainId": 32,
"epoch": "61",
"ledgerVersion": "15709644",
"oldestLedgerVersion": "0",
"ledgerRimestamp": "1664157504665254",
"nodeRole": "full_node",
"oldestBlockHeight": "0",
"blockHeight": "4799161"
},
"supportChain": {
"aptos": "Aptos(我是描述,不要把我当作Key)"
},
"supportLanguage": {
"zh-TC": "繁體中文(我是描述,不要把我当作Key)",
"en": "English(我是描述,不要把我当作Key)"
},
"supportText": {
"3": "value1",
"s": "value2",
"ebb39c4a-81f5-4676-b26a-7ce29c195755": "NFT，全称为Non-Fungible Token，指非同质化代币，是用于表示数字资产（包括jpeg和视频剪辑形式）的唯一加密货币令牌。NFT可以买卖，就像有形资产一样。",
"675eead7-9224-4f57-9202-c16e69d820b8": "Chill Kiko是Kiko全球首批系列盲盒，限量500份，每個盲盒都將開出獨一無二的 Kiko，幸運的話，還能抽到超稀有chill kiko。在這裏，您可以擁有一隻私人訂製的 Kiko。",
"5fef9c48-b647-4354-b3fa-3b3b6e449e1e": "每人購買盲盒數不超過xx個\n（1）完成錢包代幣交互，顯示【購買成功】後，點擊【我的NFT】可查看盲盒。\n（2）未打開的盲盒可選擇直接上架出售，或選擇打開盲盒。\n（3）打開後的盲盒可選擇直接上架出售，或收藏。\n（4）用戶可以在【市場】中直接報價競拍上架出售的【已打開盲盒】和【未打開盲盒】，并更改报价，修改报价需大于最高出价（最小单位：1STC）。\n（5）上架後的盲盒也可選擇【取消出售】，對商品進行下架。"
},
"supportContract": {
"tokenPrimaryExchange": "0x1::TokenPrimaryExchange::等待填入实际数据",
"tokenSecondExchange": "0x1::TokenSecondExchange::等待填入实际数据",
"nftPrimaryExchange": "0x1::NFTPrimaryExchange::等待填入实际数据",
"nftSecondExchange": "0x1::NFTSecondExchange::等待填入实际数据"
}
}
}`


###**查询boxGroup列表接口**
| 方法 | GET |
|----|----|
| 路径 | /aptos/kiko/v1/nft/boxGroup |
响应
`{
"code": 200,
"ts": 1665454609836,
"msg": "success",
"data": [
{
"id": 1,
"chain": "1",
"split": false,
"tokenId": 0,
"meta": "",
"body": "",
"displayName": "***",
"currentSupply": "10",
"totalSupply": "100",
"supportToken": "[{\"address\":\"0x1::coin::CoinStore<0x1::aptos_coin::AptosCoin>\",\"name\":\"APT\",\"precision\":9,\"price\":1000000}]",
"uri": "",
"description": "***",
"rule": "***",
"maxAmount": 0,
"creatorId": 0,
"creatorAddress": "",
"enabled": true,
"sort": 0,
"saleTime": "2022-10-10T18:03:02.000+00:00",
"endTime": "2022-10-10T18:03:02.069+00:00",
"mtime": "2022-10-10T18:05:40.112+00:00",
"ctime": "2022-10-08T23:29:56.105+00:00"
}
]
}`

响应说明

| 名称 | 类型 |描述|
|----|----|----|
| currentSupply | int |当前发售数量|
| totalSupply | int |总共发售数量|
| supportToken | string |Json数组.address:代表代币地址;name:代币名称;precision:代币精度;price:价格|

