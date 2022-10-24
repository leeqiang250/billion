## 一、host

http://52.77.131.111:8890/

## 二、header

context: {"chain":"aptos", "language":"zh-TC"}

## 三、合约接口
### 盲盒公售-盲盒购买接口
@primary_market::primary_market::buy<AskToken, BidToken>(
account: &signer,
amount: u64
)

### 我的盲盒-开盲盒接口
@kiko_owner::help::open_box<Box>(account: &signer)

### 我的盲盒-盲盒一口价售卖接口
@secondary_market::secondary_market::box_sell_fix_price<AskToken, BidToken>(
account: &signer,
price: u64,
amount: u64
)

### 我的盲盒-盲盒拍卖接口
@secondary_market::secondary_market::box_sell_auction<AskToken, BidToken>(
account: &signer,
price: u64,
amount: u64,
dead_ts: u64
)

### 我的NFT-NFT一口价售卖接口
@secondary_market::secondary_market::nft_sell_fix_price<BidToken>(
account: &signer,
price: u64,
creator: address,
collection: String,
name: String,
property_version: u64
)

### 我的NFT-NFT拍卖接口
@secondary_market::secondary_market::nft_sell_auction<BidToken>(
account: &signer,
price: u64,
dead_ts: u64,
creator: address,
collection: String,
name: String,
property_version: u64
)

### 二级市场-盲盒一口价购买接口
@secondary_market::secondary_market::box_buy_fix_price<AskToken, BidToken>(
account: &signer,
id: u64
)

### 二级市场-盲盒拍卖出价接口
@secondary_market::secondary_market::box_buy_auction<AskToken, BidToken>(
account: &signer,
id: u64,
price: u64
)

### 二级市场-盲盒拍卖吃单接口
@secondary_market::secondary_market::box_accept_bid<AskToken, BidToken>(
account: &signer,
id: u64
)

### 二级市场-NFT一口价购买接口
@secondary_market::secondary_market::nft_buy_fix_price<BidToken>(
account: &signer,
id: u64
)

### 二级市场-NFT拍卖出价接口
@secondary_market::secondary_market::nft_buy_auction<BidToken>(
account: &signer,
id: u64,
price: u64
)

### 二级市场-NFT拍卖吃单接口
@secondary_market::secondary_market::nft_accept_bid<BidToken>(
account: &signer,
id: u64
)

### NFT拆解接口

### NFT合成接口


## 四、后端接口

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

### 查询代币余额接口

| 方法 | GET |
|----|----|
| 路径 | /aptos/kiko/v1/resource/getBalance/{account}/{coinType}|

举例：查询0x4cd5040c25c069143f22995f0deaae6bfb674949302b008678455174b8ea8104账户下0x1::aptos_coin::AptosCoin代币的余额

http://localhost:8889/aptos/kiko/v1/aptos/resource/getBalance/0x4cd5040c25c069143f22995f0deaae6bfb674949302b008678455174b8ea8104/0x1::aptos_coin::AptosCoin

响应

`{
"code": 200,
"ts": 1665998324267,
"msg": "success",
"data": {
"type": "0x1::coin::CoinStore<0x1::aptos_coin::AptosCoin>",
"data": {
"coin": {
"value": "50000"
},
"frozen": false
}
}
}`

### 查询合约列表接口

| 方法 | GET |
|----|----|
| 路径 | /aptos/kiko/v1/contract|

响应
`{
"code": 200,
"ts": 1666166013222,
"msg": "success",
"data": {
"secondary_market": "0x1c87ad158f251d661cbacb167e0e459ab1ab43e1a3ca61edbf548f1cc6b23b11::secondary_market",
"primary_market": "0x1c87ad158f251d661cbacb167e0e459ab1ab43e1a3ca61edbf548f1cc6b23b11::primary_market"
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
| 路径 | /aptos/kiko/v1/nft/meta/getAttributeValue/{nftMetaId} |

响应

`{
"code": 200,
"ts": 1666320352246,
"msg": "success",
"data": [
{
"type": "衣服",
"key": "蓝色",
"value": "10"
},
{
"type": "性别",
"key": "女",
"value": "10"
},
{
"type": "肤色",
"key": "黄色",
"value": "20"
}
]
}`

响应参数说明

| 名称 | 类型 |描述|
|----|----|----|
| type | String |属性类型名称|
| key | String |属性值|
| value | String |分值|

### 查询单个盲盒信息接口

| 方法 | GET |
|----|----|
| 路径 | /aptos/kiko/v1/nft/boxGroup/getBoxById/{boxId} |
响应
`{
"code": 200,
"ts": 1666318786591,
"msg": "success",
"data": {
"id": 39,
"chain": "aptos",
"displayName": "345f4153-7cca-41ef-b3fb-b120f7a36023",
"nftGroup": 1,
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
"amount": "0",
"bidToken": {
"transactionStatus": "status_3_success",
"transactionHash": "",
"transactionStatus_": "STATUS_3_SUCCESS",
"id": 1,
"chain": "aptos",
"moduleAddress": "0x1",
"moduleName": "aptos_coin",
"structName": "AptosCoin",
"name": "AptosCoin",
"symbol": "AptosCoin",
"decimals": 8,
"displayDecimals": 2,
"uri": "",
"isShow": true
},
"price": "1000",
"description": "90f6ca41-5a27-4589-a210-ddec6b97c07d",
"rule": "",
"ts": "1665729602488",
"sort": 0
}
}`


### 盲盒公售列表接口

| 方法 | GET |
|----|----|
| 路径 | /aptos/kiko/v1/nft/boxGroup/getSaleList/{pageStart}/{pageLimit} |
说明：
pageStart：起始页
pageLimit：分页条数

响应

`{
"code": 200,
"ts": 1666238749588,
"msg": "success",
"data": {
"pages": 1,
"total": 5,
"currentPage": 1,
"boxGroupList": [
{
"id": 39,
"chain": "aptos",
"displayName": "345f4153-7cca-41ef-b3fb-b120f7a36023",
"nftGroup": 1,
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
"amount": "0",
"bidToken": {
"transactionStatus": "status_3_success",
"transactionHash": "",
"transactionStatus_": "STATUS_3_SUCCESS",
"id": 1,
"chain": "aptos",
"moduleAddress": "0x1",
"moduleName": "aptos_coin",
"structName": "AptosCoin",
"name": "AptosCoin",
"symbol": "AptosCoin",
"decimals": 8,
"displayDecimals": 2,
"uri": "",
"isShow": true
},
"price": "1000",
"description": "90f6ca41-5a27-4589-a210-ddec6b97c07d",
"rule": "",
"ts": "1665729602488",
"sort": 0
}
]
}
}`
响应参数说明

| 名称 | 类型 |描述|
|----|----|----|
| pages | int |总页数|
| total | int |总条数|
| currentPage | int |当前页数|
| askToken | object |盲盒token信息|
| amount | int |数量|
| bidToken | object |支付token信息|
| price | int |价格|
| ts | int |起售时间戳|

### 我的NFT列表接口

| 方法 | GET |
|----|----|
| 路径 |/aptos/kiko/v1/nft/meta/myNfts/{account}/{saleState} |
saleState:售卖状态。onSale:售卖中;unSale:未出售

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
| 路径 |/aptos/kiko/v1/nft/boxGroup/getMyBox/{account}/{saleState} |
saleState:售卖状态。onSale:售卖中;unSale:未出售

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

### 查询二级市场盲盒详情
| 方法 | GET |
|----|----|
| 路径 |/aptos/kiko/v1/nft/boxGroup/getBoxById/{groupId}/{saleState}/{orderId} |
saleState:售卖状态。onSale:售卖中;unSale:未出售
orderId:非必填，为出售中状态的订单id

响应
`{
"code": 200,
"ts": 1665888505470,
"msg": "success",
"data":{
"id": 2,
"chain": "aptos",
"coinId": "0x1c87ad158f251d661cbacb167e0e459ab1ab43e1a3ca61edbf548f1cc6b23b11::box::BoxV1",
"name": "BoxV1",
"symbol": "BoxV1",
"decimals": 0,
"displayDecimals": null,
"uri": ""
}
}`



### 二级市场列表接口

| 方法 | GET |
|----|----|
| 路径 |getMarketList/{condition}/{order}/{orderType}/{pageStart}/{pageLimit} |

参数说明

| 名称 | 类型 |描述|
|----|----|----|
| condition | string |检索条件:ALL:全部;NFT:NFT,BOX:盲盒|
| order | string |排序字段，ts:上架时间; bid_amount:价格; dead_ts:截止时间|
| orderType | string |排序类型：asc:顺序; desc:倒序|
| pageStart | int |查询开始页数 从1开始|
| pageLimit | int |每页条数|

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