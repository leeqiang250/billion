## 一、host

http://52.77.131.111:8890/

## 二、header

context: {"chain":"aptos", "language":"zh-TC"}

## 三、接口

### config基础信息查询接口

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

### 查询boxGroup列表接口

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

### 根据group查询nftInfo列表接口

| 方法 | GET |
|----|----|
| 路径 | /aptos/kiko/v1/nft/info/getListByGroup/{type}/{groupId} |

请求参数说明

| 名称 | 类型 |描述|
|----|----|----|
| type | String |group:nft系列;boxGroup:盲盒系列|
| groupId | String |系列id|

响应

`{
"code": 200,
"ts": 1665458346095,
"msg": "success",
"data": [
{
"transactionStatus": "status_3_success",
"transactionStatus_": "STATUS_3_SUCCESS",
"id": 20000006138,
"boxGroupId": 0,
"nftGroupId": 1,
"displayName": "***",
"description": "***",
"owner": "0x4cd5040c25c069143f22995f0deaae6bfb674949302b008678455174b8ea8104",
"uri": "https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/f4c63b48-eed9-4707-3ebd-3149b7a99700/public",
"rank": 0,
"transactionHash": "0x289352539772d97faec8128c1dcacb432713652bdf916437e0cc8749bd575186",
"isBorn": false,
"nftId": "",
"score": "",
"attributeType": 0,
"tableHandle": "0x38cea7aa11ed8a4bf274da7d2abec2f996a195efa22a81bc87317f63b6a69a58",
"tableCollection": "0x62363965316661652d366434642d346334322d613938632d65",
"tableCreator": "0x4cd5040c25c069143f22995f0deaae6bfb674949302b008678455174b8ea8104",
"tableName": "0x31333733613835312d393437372d346663392d393166612d345f"
}
]
}`

响应参数说明

| 名称 | 类型 |描述|
|----|----|----|
| attributeType | int |属性类型(0:计分;2:不计分)|
| groupId | String |系列id|

### 根据nftinfo查询nft属性列表接口

| 方法 | GET |
|----|----|
| 路径 | /aptos/kiko/v1/nft/class/getByNftId/{infoId} |

响应

`{
"code": 200,
"ts": 1664357874191,
"msg": "success",
"data": [
{
"id": 1,
"nftGroupId": 1,
"nftInfoId": 20000006141,
"className": "衣服",
"type": 1,
"score": "0",
"attributes": [
{
"id": 47,
"nftClassId": 1,
"attribute": "颜色",
"value": "红色"
}
]
}
]
}`

响应参数说明

| 名称 | 类型 |描述|
|----|----|----|
| className | String |类型名称|
| attribute | String |属性|
| value | String |属性值|

### 盲盒公售列表接口

| 方法 | GET |
|----|----|
| 路径 | /aptos/kiko/v1/nft/boxGroup/getSaleList |

响应

`{
"code": 200,
"ts": 1665921067059,
"msg": "success",
"data": [
{
"id": 43,
"chain": "aptos",
"displayName": "名称43",
"nftGroup": 5,
"askToken": {
"transactionStatus": "status_3_success",
"transactionHash": "0xd89288406cda2649cb322afca1ca092bc35b349204a8e41975ed7e8d64366650",
"transactionStatus_": "STATUS_3_SUCCESS",
"id": 6,
"chain": "aptos",
"moduleAddress": "0x1c87ad158f251d661cbacb167e0e459ab1ab43e1a3ca61edbf548f1cc6b23b11",
"moduleName": "box",
"structName": "BoxV5",
"name": "BoxV5",
"symbol": "BoxV5",
"decimals": 0,
"displayDecimals": 0,
"uri": "",
"isShow": true
},
"amount": "0",
"bidToken": {
"transactionStatus": "status_3_success",
"transactionHash": "0xf01a5ac0260bb031068d28b57143a2ab6d76fd9908975203373222d14eda73d4",
"transactionStatus_": "STATUS_3_SUCCESS",
"id": 7,
"chain": "aptos",
"moduleAddress": "0x1c87ad158f251d661cbacb167e0e459ab1ab43e1a3ca61edbf548f1cc6b23b11",
"moduleName": "box",
"structName": "Win",
"name": "Win",
"symbol": "WIN",
"decimals": 8,
"displayDecimals": 2,
"uri": "",
"isShow": true
},
"price": "574",
"description": "描述43",
"rule": "***",
"ts": "1665729608198",
"sort": 0
}
]
}`

### 我的NFT列表接口

| 方法 | GET |
|----|----|
| 路径 |/aptos/kiko/v1/nft/meta/myNfts/{account}} |

响应

`{
"code": 200,
"ts": 1665921749869,
"msg": "success",
"data": [
{
"transactionStatus": "status_3_success",
"transactionHash": "0x14075727132f19b8dfd13de74aea60e41b3c4a0bff51f5e9856835d19d5752fb",
"transactionStatus_": "STATUS_3_SUCCESS",
"id": 20000006595,
"nftGroupId": 1,
"displayName": "213a20f4-2c9e-42b9-947e-8f5ecccf8037",
"description": "12fc1185-81a0-422f-a842-c19253415090",
"uri": "https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/4031cc60-3e88-4f78-b412-5006ecf5c100/public",
"rank": 0,
"isBorn": false,
"tokenId": "0x1c87ad158f251d661cbacb167e0e459ab1ab43e1a3ca61edbf548f1cc6b23b11@0xe5908de7a7b031@0x33302de68f8fe8bfb03230303030303036353935@0",
"score": "",
"attributeType": 0,
"tableHandle": "0xec4d7086e662c621c5011deb383abe67c9f04ffab889b52ddb0e8008363e4503",
"tableCollection": "0xe5908de7a7b031",
"tableCreator": "0x1c87ad158f251d661cbacb167e0e459ab1ab43e1a3ca61edbf548f1cc6b23b11",
"tableName": "0x33302de68f8fe8bfb03230303030303036353935"
}
]
}`

### 我的盲盒列表接口

| 方法 | GET |
|----|----|
| 路径 |/aptos/kiko/v1/nft/boxGroup/getMyBox/{account}} |

响应

`{
"code": 200,
"ts": 1665888505470,
"msg": "success",
"data": [
{
"id": 2,
"chain": "aptos",
"coinId": "0x1c87ad158f251d661cbacb167e0e459ab1ab43e1a3ca61edbf548f1cc6b23b11::box::BoxV1",
"name": "BoxV1",
"symbol": "BoxV1",
"decimals": 0,
"displayDecimals": null,
"uri": ""
}
]
}`

### 二级市场列表接口

| 方法 | GET |
|----|----|
| 路径 |/aptos/kiko/v1/market/getMarketList/{pageStart}/{pageLimit}} |

响应

`{
"code": 200,
"ts": 1665921494066,
"msg": "success",
"data": {
"pages": 1,
"total": 1,
"currentPage": 1,
"marketList": [
{
"id": 57,
"chain": "aptos",
"orderId": "16",
"type": "fix_price",
"price": "999",
"maker": "0x1c87ad158f251d661cbacb167e0e459ab1ab43e1a3ca61edbf548f1cc6b23b11",
"askToken": {
"transactionStatus": "status_3_success",
"transactionHash": "0xd1de62fd5d67b05d61be44f63cd7ae242e2766ae288f92aa3c82aa2d533459c7",
"transactionStatus_": "STATUS_3_SUCCESS",
"id": 2,
"chain": "aptos",
"moduleAddress": "0x1c87ad158f251d661cbacb167e0e459ab1ab43e1a3ca61edbf548f1cc6b23b11",
"moduleName": "box",
"structName": "BoxV1",
"name": "BoxV1",
"symbol": "BoxV1",
"decimals": 0,
"displayDecimals": 0,
"uri": "",
"isShow": true
},
"askAmount": "1",
"bidder": "",
"bidToken": "0x1::aptos_coin::AptosCoin",
"bidAmount": "",
"ts": "7200",
"deadTs": "0",
"orderType": 0
}
]
}
}`