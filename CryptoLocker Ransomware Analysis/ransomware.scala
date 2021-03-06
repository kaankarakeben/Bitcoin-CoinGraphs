
import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.util.IntParam
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql._
import org.apache.spark.sql.functions._


//reading in the separate files
val txFile = "/data/bitcoin/transactions.csv"
val voutFile = "/data/bitcoin/vout.csv"
val sqlContext = new SQLContext(sc)
val tx_df = sqlContext.load("com.databricks.spark.csv", Map("path" -> txFile, "header" -> "true"))
val vout_df = sqlContext.load("com.databricks.spark.csv", Map("path" -> voutFile, "header" -> "true"))
tx_df.registerTempTable("tx")
vout_df.registerTempTable("vout")

// First table: joining transactions and vout
val table1 = sqlContext.sql("select a.tx_hash, a.time, b.pubkey, b.value from tx a, vout b where a.tx_hash=b.hash")
table1.registerTempTable("table1")

// Creating the DataFrame for know publickeys for CryptoLocker
// Vertex DataFrame
val ransomware = sqlContext.createDataFrame(List(
("CryptoLocker", "{1KP72fBmh3XBRfuJDMn53APaqM6iMRspCh}"),
("CryptoLocker", "{1147GrWQh7yW91kV65QP8BhFiGQQKuW3n5}"),
("CryptoLocker", "{125FCpENaKRu95psPP8gETM9u2EmvfAhF2}"),
("CryptoLocker", "{125vS1drhzddwkCAZfrMUjxMBU8xJ7Be9v}"),
("CryptoLocker", "{127tTWffUF93qp9jBjPiayBGpiKG9JgMfC}"),
("CryptoLocker", "{128ApausYAG5hCiRjzY5sKnaLAU7YSavC3}"),
("CryptoLocker", "{129U88iERn5dpjm2gdeaJBQWA87zeiULY2}"),
("CryptoLocker", "{12auVFnXMMizCSAfFLXYpgtkAgvgpC34np}"),
("CryptoLocker", "{12bVTY5ZkmGXQoTYFgLrF7xdQeAKto6DT3}"),
("CryptoLocker", "{12cZEAGkTCnHmvgKP4GK1jXq36WrUkDiMh}"),
("CryptoLocker", "{12dGtJF5dM4QPZfYwZk7nsY8ABRZo4LBJh}"),
("CryptoLocker", "{12dvJyX6pzPxNvTi9u11RpDARHXsCjuy75}"),
("CryptoLocker", "{12EEdNrva2FgqMHv33Maj8mNXXgWRy17Lq}"),
("CryptoLocker", "{12FnQgEWdqkKcRRM3TzW31SagyDBR776xf}"),
("CryptoLocker", "{12GtimBbqDvXrT5ExxoyWMKQHwwGSvg5iZ}"),
("CryptoLocker", "{12i8GBDDLx93AnCdsGQTdN46EcDAz39vXa}"),
("CryptoLocker", "{12iz6uan4nWLU4xpuebxjdM8oJqcvKGM62}"),
("CryptoLocker", "{12k4mMYpaX6vgNFzL7qnFhsvy9BmHMUn1J}"),
("CryptoLocker", "{12Lk5fHNJiR1YVR7aEHyF69fXqSnFcJUxb}"),
("CryptoLocker", "{12mPaFLrUsqr18eh5LxJ2vjda4Tj79YNzB}"),
("CryptoLocker", "{12N1Z763yEMqKGGXTmWmMunhVim5CggcrX}"),
("CryptoLocker", "{12PXDTJFV84AWLoCiFNcd2Vd6wmukZWEDu}"),
("CryptoLocker", "{12QdTVrr9PodpSnP6uoFcJmudUVR6DNFBj}"),
("CryptoLocker", "{12vNSdg6TeS259ZJBuJu2xZrAqVkmNqSu9}"),
("CryptoLocker", "{12wQNVBEXuynSvXivTBstqwobjbRUJiHTg}"),
("CryptoLocker", "{12xkY3XezVZUsetYYxutJSUUSLQ2byyRBJ}"),
("CryptoLocker", "{12yzWvwsqkW6bAUY4pN2mrUmQo6hRjXujb}"),
("CryptoLocker", "{12Z71WcLDC6pLQiyWATU2xx91455PeE3ki}"),
("CryptoLocker", "{12zK22ydWiUWLVcG3DXZsX8crFMv82R9u6}"),
("CryptoLocker", "{12zWkbSTuKKe2mKgKJrrmZ5hm2xCDZJsQC}"),
("CryptoLocker", "{132ckcKGxofNhZfG7bm9dv8kZGtVpNHwpr}"),
("CryptoLocker", "{132R8sHpyvdchXJjPD8cpsA6Pw4gmWrJS1}"),
("CryptoLocker", "{137tDrDAoh24crTwNqTcAa3HanFVupE2dB}"),
("CryptoLocker", "{13axjN3e7hRSKwP1SUcbvU2Mk6oJi1pdM5}"),
("CryptoLocker", "{13B4z9sf3NcwopkcX2ZHz1eu67XgtHr3yR}"),
("CryptoLocker", "{13CHWK2e4gB7CS9KdR1cNy1J8mwj6E6AAL}"),
("CryptoLocker", "{13CtRbwChs6jrC3WDP7XxU5EzSmQ3eAhvP}"),
("CryptoLocker", "{13envUEGi1d2PdErDVu8Wym7jcDh1YwgqK}"),
("CryptoLocker", "{13HPsTsERexydQNNkMPjjCPixFvxWpgPs1}"),
("CryptoLocker", "{13Hs65DrTNJfGC7Nvdvv1WL2kMf3vXpdCf}"),
("CryptoLocker", "{13ĻBrrwbFLTyL2J7Vnh5QoM5jBJAB1quT}"),
("CryptoLocker", "{13LDpTY8c74hBvxiSEJKaMQg8zmzYJ74rS}"),
("CryptoLocker", "{13LihD38jd8Tsmr6YZLAqFMSDWHebXe88N}"),
("CryptoLocker", "{13MjaSU6vKCNgWLB9dVi2gZ4LNeAUSokMr}"),
("CryptoLocker", "{13N36rkC9Pbei3Z2fyNYvLd8wJwMxdXji8}"),
("CryptoLocker", "{13NKTcbc7Tjhs2cSywwDF4qTihqdW5FJzu}"),
("CryptoLocker", "{13o3SLKPZkpphJGa5jcLBe4WrpMPJbrG4s}"),
("CryptoLocker", "{13oEMXkrxQ7bzHhNNp9i2qW8ngEibt34w2}"),
("CryptoLocker", "{13oy8a8Ytfwue3oAR3nBG796nFNyKnmhMg}"),
("CryptoLocker", "{13oyQKhsb1PhEMfNyj2fEkaDMbhKCpWv3j}"),
("CryptoLocker", "{13PBA8pCYJTKWTVSzG1kcVfKhJ2cKQPKhu}"),
("CryptoLocker", "{13QfgCjpBd6vWwjf4NPa6B8DejnjozYhCQ}"),
("CryptoLocker", "{13RZBcRRFWoee6mMxBjB6NuB4PU6U9ZQay}"),
("CryptoLocker", "{13S4DGMrq2FNeMCR6w3TQwVGGDN641kMTK}"),
("CryptoLocker", "{13Tb67ue2X9zSb6ngopgp27DL6gV4YUPZ6}"),
("CryptoLocker", "{13tvAGyKMGeUPck4yUFKffvWGTDESZE6xV}"),
("CryptoLocker", "{13wLbTLLkUgZCkeREDQEzmrhms4hQtqViY}"),
("CryptoLocker", "{13wWnCAZUKM2eBb9fYKfkenK4wsQKiiUPx}"),
("CryptoLocker", "{13yhUDumXgcaoSe49GUjwCPYbrcibEhCx6}"),
("CryptoLocker", "{13YooSYj267bUgRBny74esCGm4VKcMsJAE}"),
("CryptoLocker", "{13Z9opdJAckCnLuTJ17wkWvUBbwbmeuPdG}"),
("CryptoLocker", "{13zgH2V2LBdg4hC6nemhUc6VAj2HYa1FZb}"),
("CryptoLocker", "{13Zim8VJZTxYM8zrDH9DnjMt1hmkJR4ozn}"),
("CryptoLocker", "{142JLE7k21dubJWKumgeFRA8cBga9BEWUV}"),
("CryptoLocker", "{143nAiwSq6GzwhadnwBŀWNLaEu5Lar7tr}"),
("CryptoLocker", "{1445gF1w48JC6Mbg5WjHphqvNPt7Axpj3M}"),
("CryptoLocker", "{144eFKYQJ2hUL9hJ9v9S25X453Cbtpvej1}"),
("CryptoLocker", "{149DHg7KuLX5132CELLcf1zJ1tAi68mFNo}"),
("CryptoLocker", "{14AX3nqeZFaet3MbKtFWcQVCT3yJAT6pzC}"),
("CryptoLocker", "{14bh9uqKzgqV6yqTqHbU2bdbpY3a8pktzR}"),
("CryptoLocker", "{14ETSyJ5Mvi3nXvX8HUjhhr3LSzwqb38Qa}"),
("CryptoLocker", "{14FMhAYUJ5CSTTTLGNZpZUWfrA4dgkakeG}"),
("CryptoLocker", "{14FQo3JR4zFg3PxX36F74SY4Vm47JFuiAw}"),
("CryptoLocker", "{14FTQGLQEwndyctnHSxwDVMHmKnjER3DqR}"),
("CryptoLocker", "{14GkLnmsjpsDUY7UJvPFy5BG1cUPLpiTCR}"),
("CryptoLocker", "{14H4ZyQVM39jCJHyPaYtJ1uay45PuzxyHS}"),
("CryptoLocker", "{14HBfQk71U44ov8Tg7WgdEKutm3nP5oqrB}"),
("CryptoLocker", "{14jDrNMDJi2MPnBJVWTZwB6aU4A9be3Ļd}"),
("CryptoLocker", "{14KE194E9kfxxjYCZrSCXQFuC1qfHsu4zL}"),
("CryptoLocker", "{14oc1oA2uuybtFUnyxJVkwKtDyNk5gXgDv}"),
("CryptoLocker", "{14RPC2uw5cWDpsLj8PXjCm4LkiQpNViDjS}"),
("CryptoLocker", "{14SyfoJmS2gQNvLcZw1NV4RbPWL5wRChDY}"),
("CryptoLocker", "{14ujLQwtnQjnxyp7KtyHKhj8dsgoBkTwcb}"),
("CryptoLocker", "{14UNup9ys1w5HCC3zEj7nynF7aSFCJiyNQ}"),
("CryptoLocker", "{14UPJZhzXV6CEty3c9RR9XkLGa4pLkEfWp}"),
("CryptoLocker", "{14uW4eDNRPUZW5MzE3Cbbsn7DkzBSTs36x}"),
("CryptoLocker", "{14UWPLULEXvtUz8Pc46AJTCLmFpg7FS9LN}"),
("CryptoLocker", "{14y9mdPgoHKW4jAGniWiKJa1dHsKoMHnr9}"),
("CryptoLocker", "{14zcNgMGuxWv5bb91jqibshwX8d5Q954Bq}"),
("CryptoLocker", "{152k2fQnttYX9BLTnHge9mnJeZNeKkwX7t}"),
("CryptoLocker", "{153RFdk3UJ1pZqd4hBVeguiPxphEfoujyG}"),
("CryptoLocker", "{1544c47YAMaHtgqmNmy86hPjJrmEaXxvdC}"),
("CryptoLocker", "{1548XtfEVgbKjkzbB3KeZUcmf8vyWWZcae}"),
("CryptoLocker", "{154CfE38JJdkGK7jB7iDAXPFRGVmCSZLCb}"),
("CryptoLocker", "{1592QxvdaHbFuAoiXaR7BBnmKrGtwLNVBU}"),
("CryptoLocker", "{15A33N6NYFZXxB7xhhDZ83i2ATXoczBMKL}"),
("CryptoLocker", "{15aXu2EhAvpWDEcoWEcWeG73XCNe7vRjz7}"),
("CryptoLocker", "{15dv1zNWJEa6t7dcSXGa9JDCUPFYNeGo6G}"),
("CryptoLocker", "{15eQyTL43nxzPPbLMf28UsbbXPeKKkZ8gV}"),
("CryptoLocker", "{15GPKoAvNjsK6Fzb1VmF6bxL8MmYcwUJas}"),
("CryptoLocker", "{15GStXVRzaFEvJtA41CKfd5WmxPiEZ2fSe}"),
("CryptoLocker", "{15K49Q54yrHgJCKs33RsdaGhJh4Cge99MW}"),
("CryptoLocker", "{15Kgbsv1GpF2fHcV3t1xBKoYk9RywwDWe3}"),
("CryptoLocker", "{15kXk9a3LbtECeMHpWE2AqTvPcu9VTaTFp}"),
("CryptoLocker", "{15LVzgJ27r8utkvVh3eu11YQ61s9TvWQib}"),
("CryptoLocker", "{15MExBr8yweakyY5jDJz3mZVuuDB5KrUUW}"),
("CryptoLocker", "{15NaroT4CuEVswUcMvSHMVXeJynSd5LZVr}"),
("CryptoLocker", "{15ouuBb9rwfevxzDWKYYc2yNNPnŁjGeN7}"),
("CryptoLocker", "{15qkjgvS11awYjyC7YoZ3KCgkXuNwQRaXv}"),
("CryptoLocker", "{15rkjyqBoKWTjbQSb12jCxAyBEYHm515GK}"),
("CryptoLocker", "{15SMH5YbWuixjghZ5AubhWDkRFNfLLkS9Z}"),
("CryptoLocker", "{15UuRJce34S4NqaEwkCrdw3BFSFdB4TyZt}"),
("CryptoLocker", "{15VjFAytgecHhjZXkNkbGdiC4sUEAKxuux}"),
("CryptoLocker", "{15vKEVHĻcmCCepzx91AQTFbD1bp6SNLi7}"),
("CryptoLocker", "{15XVKc4MPNxeHmdfWjX5nVFdT2X9hhuxfL}"),
("CryptoLocker", "{15ypJ3NZB38SWsvA2AKGd8vCTp5kjodR6a}"),
("CryptoLocker", "{165d1uBCceZvfG2RXkRHJY3px4GYNdTBtM}"),
("CryptoLocker", "{166yMjKQsEFngzBr4Ds459dmtWY4hGepQ5}"),
("CryptoLocker", "{168bWbZF6VcQnVm1wBoeKWKPyhy771W6dx}"),
("CryptoLocker", "{16aGi4L4navwgK3nyGzkFuXbSu3HYPtJHL}"),
("CryptoLocker", "{16AZ2L7HiKwTxG3chtzKs5qQQPeexkEELA}"),
("CryptoLocker", "{16c1UiYKg72aL5Y2wopkuWgMC8rSHhgWZr}"),
("CryptoLocker", "{16ffTM4mix7CWRjAF4WEgeRTWoMifyarkN}"),
("CryptoLocker", "{16gNsTYPz64LxXzx4hi3PEvbS1EBMa2zVr}"),
("CryptoLocker", "{16iLGNLtrjd9AvcwbFD71DcGGgFŀNsE52}"),
("CryptoLocker", "{16jMEqCjJn91ckzbw7MpKmc2ivG5PguYTB}"),
("CryptoLocker", "{16kPwnKPzVhZFynr7PVNooapx14o6btrsx}"),
("CryptoLocker", "{16LVKAnvfKgujUBU1PYSisoi33YDeA141d}"),
("CryptoLocker", "{16mxyjFfFtQxUR7tnUCfdbqkHCkhv95AFc}"),
("CryptoLocker", "{16p1aBpxfAcW4R5MabhBjfGbEosdT3SHfq}"),
("CryptoLocker", "{16PDrs2uZncAcW95uo9bCiKqR6wc2kWpun}"),
("CryptoLocker", "{16pWYY5LnZZnZQd6cy2bJkGzic2WudBKMc}"),
("CryptoLocker", "{16sd6wwNke2AXMKgmjJ3yMXivmEog8xAfq}"),
("CryptoLocker", "{16sDE6YYpCihXnAjRuSNCNNPiZo6wWEvAq}"),
("CryptoLocker", "{16uYjskZ5e85aZfrbYVM9fthDTrhTW9GbV}"),
("CryptoLocker", "{16Vn4aq2d7TXE4XqyQ5dbcZGTHAQhWZn8U}"),
("CryptoLocker", "{16vo4KisPbHmmNXq3ekAdtMDC1oKBxsxwT}"),
("CryptoLocker", "{16XfNSRrUuLy198aFX3F13shEoRPzNcKdY}"),
("CryptoLocker", "{16XyoG3yB2SvWXFdQ2fy17sytCmf2RBk1b}"),
("CryptoLocker", "{16y6pGWVqbmJxN4ADT9PAqoUctWz2WbmuL}"),
("CryptoLocker", "{16YH8udBjRPMLGiuWrAfGDAHMTQeVX6bm2}"),
("CryptoLocker", "{16z2bKh7tLyxKKcM6wutqTGB7MyHPPR3ym}"),
("CryptoLocker", "{16Zaif2w3W7JHktZqvgrnNk7up2kFi914E}"),
("CryptoLocker", "{16zenZ1MCjchhEN9v1cJ3wz7yMqvCZftbx}"),
("CryptoLocker", "{172fBq95Vw5qzR6TpNQDCjuj6Q3EiCPw7V}"),
("CryptoLocker", "{173eCvWDT3PBZM8a4Fiac7dHTjJTYfNJry}"),
("CryptoLocker", "{177HgELfgYkwNoXn3B4tMfA9X6bECwdpde}"),
("CryptoLocker", "{179dwoQeeDyiK8fdR14Efm7rNGm14bZcfW}"),
("CryptoLocker", "{17d3wUL8m2gmi3CAYB77n381vK6z213M5u}"),
("CryptoLocker", "{17fK93pKA6eAmqBB4vefWyi1AXyjTYr5ZY}"),
("CryptoLocker", "{17HsiiF9q5hzQLP31stAigDSrRBGqj7uXz}"),
("CryptoLocker", "{17kEVhBMqJB67sYCJTJ4A3qfyB1MNL87MC}"),
("CryptoLocker", "{17Lx64PZ7uEV3owFgPTAUA8tcA8Quhi4zf}"),
("CryptoLocker", "{17mdKEAqspuuvtS6kcZZft7JBjscAQLjrL}"),
("CryptoLocker", "{17MKykhda15eXy9arFr6o7cPjWm5kcbK4E}"),
("CryptoLocker", "{17n1uri4Eh9uLQ1XWpjed1EjUrNkYTc7eB}"),
("CryptoLocker", "{17nKbdRW9pd7kco5fGcyHrbJxQepdWVWy1}"),
("CryptoLocker", "{17ntYg99aNqjVu4HEKxZHvp7doWXY5iHa9}"),
("CryptoLocker", "{17qvywrovR7K1WGszK4dQ1wmH4FpkiXukT}"),
("CryptoLocker", "{17QX3c8qh2zVCrRFidcLi6JEuaXx5J3EYC}"),
("CryptoLocker", "{17ssaL5PDbsZswVaKNou4pGEkwP1ZeEjs7}"),
("CryptoLocker", "{17TgFQqurkG1ze4aHzjW9mXjekywvPoHk5}"),
("CryptoLocker", "{17TmrfBEWgXhW3DC9iWw7sobsD8rUb6TMS}"),
("CryptoLocker", "{17UYrYUqn4NDfgEq2EHr5qz62WRu4DGraT}"),
("CryptoLocker", "{17w9bMf2DEcDUbpfVFodb34LpJShHdCTRv}"),
("CryptoLocker", "{17xhPSYVFhRrsxWmX1pizKHmutCRHD33g6}"),
("CryptoLocker", "{17zKbutfsJrhxLn5RZiWGYu2DqYbpRt2AL}"),
("CryptoLocker", "{185QPodi3zaSVtBjEGAALKT2sdycWxrByv}"),
("CryptoLocker", "{187Fwi9MGJ4N31hcY6YNK8L152C755HmqU}"),
("CryptoLocker", "{188G3tCxaq3WuSXTww7w821iEgUpYhwsq8}"),
("CryptoLocker", "{18Aos3Rx72XdfZVRo6TmYGHYvkjGYRFy4H}"),
("CryptoLocker", "{18BvLh5Zjma7gTa9wWLSpLjY31j4SQR5KP}"),
("CryptoLocker", "{18cB3t3ob1HhoFALSqqQPpnyVs7A8pvPsC}"),
("CryptoLocker", "{18CgGQm7gi8XNeJfcyWQnzXGyzB5HY5Sr}"),
("CryptoLocker", "{18eFXVT8goCQPAvvTEzSJab5QB5pDdDLNx}"),
("CryptoLocker", "{18EnkSexV3wjY6rmbBeziGunbP72Y9U1du}"),
("CryptoLocker", "{18epejFt7wA1qUrQPsFNt9KSwVo57mYd83}"),
("CryptoLocker", "{18ePWwRrJvnH2YAS8bN9fPF1NT1Y91KLqG}"),
("CryptoLocker", "{18GMEYE468HGTcEr5Nj2v8uC8Fiv5MNZGJ}"),
("CryptoLocker", "{18GZDbcEWxEVxcF3u8VjEsE3b3axoxJc5c}"),
("CryptoLocker", "{18H4tdfhEMgPug98pY6UDFTFMxTzqnUhhQ}"),
("CryptoLocker", "{18iEz617DoDp8CNQUyyrjCcC7XCGDf5SVb}"),
("CryptoLocker", "{18ixwb5si3AQBpjkUA91vGE3ESoynXd95g}"),
("CryptoLocker", "{18KoxE5QjASmtQoanJa2AFphtjqWvYJabx}"),
("CryptoLocker", "{18Mfsc5RVbzij1kew5HFg5nxdkDLd6knNp}"),
("CryptoLocker", "{18mWpTuUPiZXu8MxRzwNfKv57DQDfJ9azK}"),
("CryptoLocker", "{18P5UpeD5Dn9F9r2G79KXzwWvdE9Jmxvx}"),
("CryptoLocker", "{18rqSx5LMf7PYvxw8QTKgJWUV4LLsRxQxV}"),
("CryptoLocker", "{18SghQmtGjDaUre3yyhsfBajeMrEDAkEtJ}"),
("CryptoLocker", "{18SP4vGAsTpd8yGjQ9GABQu7AKVaSHhAbs}"),
("CryptoLocker", "{18TzEwoQy8JhU3m8fufXwĻEqNet9j8Yc4}"),
("CryptoLocker", "{18Ug2QUiEKqZTtFjpR6iui5oRP8Q4SkG4q}"),
("CryptoLocker", "{18upRKhpRr2GMtUW8cR6dWMNJEvzQXbQKc}"),
("CryptoLocker", "{18VQRLUWGt8dE4B5Gd6yUqzBH1vozERMUb}"),
("CryptoLocker", "{18w41e49sSx3eSzm26L6iZ3rL9BAvzsF2u}"),
("CryptoLocker", "{18WWGX3vsAr4rHNJbwkvYDadswp1LSeVww}"),
("CryptoLocker", "{18wxVCTAmN5FHUwy43fb2UnMGgASVd49nS}"),
("CryptoLocker", "{18Xi4fRqeCE9DcBx7h8Xcs3EpWZH9aZW5R}"),
("CryptoLocker", "{18ZRqw6vKj5pPff8iDaFDsfXgfkpjqiuqk}"),
("CryptoLocker", "{18zzMtaprqzSBuaFFxod73LZMz3Xspm2NX}"),
("CryptoLocker", "{193N9bw37LRjs26NmCSN8vERN76MnbNU2Z}"),
("CryptoLocker", "{193tqzCaEGbUDw114QwZYoLJvEoB3Ao5K8}"),
("CryptoLocker", "{195phaUWwSq6CEiL8avFs89FjktWbnAUq4}"),
("CryptoLocker", "{199K1rKLpCZXwLae2C6QvFa2jYMN5LFcPg}"),
("CryptoLocker", "{199mTAvoE5URLSovHDUwcJsTcviTrVEA2y}"),
("CryptoLocker", "{19bJ2pXT8cEPDS1FCYatYX5QohnV7Rnvyv}"),
("CryptoLocker", "{19Cx9zXJhfDoFsLpL5iyCMaXfCD8FAVcGQ}"),
("CryptoLocker", "{19fYob7vM6ZZb8e3cCnZXAjpPiWbPHjkDR}"),
("CryptoLocker", "{19GiP1bC5qYmyoxqBmeHcFoAdRgyk1C37K}"),
("CryptoLocker", "{19GTqXwKmMW5FKDuLh7ŁLyahq1Y2YPNmc}"),
("CryptoLocker", "{19h9GRcx1ABHPEzVpmWqqA9TTgug6JNcBc}"),
("CryptoLocker", "{19ik4sK6mGnTgqKe9EcHxgFmTyQFJQHBsi}"),
("CryptoLocker", "{19joSY2NHVpY6r5dEsjN7XJHMQV6tmT13s}"),
("CryptoLocker", "{19jZ4bUspCa6zEKHyuxvnm3yWdYLk5na7k}"),
("CryptoLocker", "{19k3qpwke9NkxKoW2VExEa1ZvfqYrqGgL1}"),
("CryptoLocker", "{19kSg47CibsBZPwABbTRQN6B8porvaaWze}"),
("CryptoLocker", "{19LHsVsHVbgtevDenVb2CNTDPM5hjxsBqU}"),
("CryptoLocker", "{19LwEWuBDqaHHG3sCmmoXqfUi7E4v8S8Kj}"),
("CryptoLocker", "{19MNqUeDa78NfuSzJttaWbXVavc7mRZqj2}"),
("CryptoLocker", "{19QiXsxYQN4QwkdDq2ee72QKVvjbCU65ye}"),
("CryptoLocker", "{19rW64ioQs43Xt1DiQXqqc5cwEpmWENMr7}"),
("CryptoLocker", "{19sUq7btf49S4gMBKES4NUa5ez8dZqX84N}"),
("CryptoLocker", "{19taqqVnko8Ujg549mS5QztYuczaAj4TVq}"),
("CryptoLocker", "{19uRAxstbHYb7t7vAJssBBGUErXanBWXnt}"),
("CryptoLocker", "{19uVk8uzQwTnLJYXTyWH5pihmpYPWdfm2Q}"),
("CryptoLocker", "{19Wem2PzT7BVyLYPxzcwULigX7ZotRoZa1}"),
("CryptoLocker", "{19X6YnufnD7zsb9GPPmseeEHmGY6ti5qzt}"),
("CryptoLocker", "{19XH5t4odCnzGKU8f2LWiA3uwHVJ7PM9L1}"),
("CryptoLocker", "{19Z2S2rGAuU93ShKvRguQdP1tZ2wh4emfp}"),
("CryptoLocker", "{1A2VrTUtBHg4ZA7mEv3YEEBNhfVgZmsdsN}"),
("CryptoLocker", "{1AB1miU4GoRQzDggszBYd1gdMZqU265AUN}"),
("CryptoLocker", "{1ADEh9XUaMegTkZnU1Y1WLqcGHg6Ng6CTQ}"),
("CryptoLocker", "{1AdeHsocAtHwk47rc4xP4L7bGGXtpTMEY9}"),
("CryptoLocker", "{1AEhRR23Pg2tzWFPSjxNW52T2DDnbRpMGT}"),
("CryptoLocker", "{1AFiCbx7HVarBgFBP5xH8BdwNJKTRk3mvr}"),
("CryptoLocker", "{1AfnpkhYunFmZWb7tw2vwgc3DB99CU9HzM}"),
("CryptoLocker", "{1AHKr7uEemLay1AfSMaMtjMiPBUvUL9EJU}"),
("CryptoLocker", "{1AhNqm4nPTLCPLHmYuPaoNiVbxbMvnYqxu}"),
("CryptoLocker", "{1Ai8sfxU2E99WiCdwHodRXBjA8rvtiBfLF}"),
("CryptoLocker", "{1AK3bMYn9pzndzKLeREE7bKvAuRyVd5f61}"),
("CryptoLocker", "{1ALzUKGxniXVkcdoBERpNre19257QH3R8R}"),
("CryptoLocker", "{1AoNBYpVUy5Me6kN7ufwaLBaFBgpGW9EgL}"),
("CryptoLocker", "{1AoU2rZrCfADR3jyKKX4roksTBnXWoGe3X}"),
("CryptoLocker", "{1ApY6uap9fCTHZZTnJLoFg5E6a1BRokfda}"),
("CryptoLocker", "{1AQGdK9vXfvP8jN1k1bG9hGk9Dfy9gfHxP}"),
("CryptoLocker", "{1AQHyJMVoro6REmNdb6q2LLmj8Ci5EQaSE}"),
("CryptoLocker", "{1ARTideV24Y8yHKfB5APPoabRffeuqdKhh}"),
("CryptoLocker", "{1At6BoXND9Bd2v58nG8ghXntNfvKA8YfXp}"),
("CryptoLocker", "{1AU4Bdf13bCkc2fTkyAQc4zA25kuJQbu8H}"),
("CryptoLocker", "{1AUvX11NzrtrZUho6yTdp7KBooyAMBcGY7}"),
("CryptoLocker", "{1AvxVKVYdi4tCuh7e6Xz3yxdGcmhURZkEF}"),
("CryptoLocker", "{1aX2zTKBnZrtxGpPA8j77V2FfCn1EGSib}"),
("CryptoLocker", "{1AXEDzWtrYHBypc7yjByF4457svsuaM9Xm}"),
("CryptoLocker", "{1AXNQvRxpLXLF2z3WrrREq3LHgdbpsAm38}"),
("CryptoLocker", "{1AyBaDLN3rgHKTcXh5jceqUNNSCwrjWJ7}"),
("CryptoLocker", "{1AZ7eyuw5YucEaqvdwWCjioMRq4jdTtHEn}"),
("CryptoLocker", "{1Azj2voL5AnDnGeND6DxjUVtv5MUNLkcok}"),
("CryptoLocker", "{1B4SjCEppw4jgJLLH6WHf7zzdp5gMLreqB}"),
("CryptoLocker", "{1B58CmceR7A9zSCtzgGp5EHy1cVT8Kg3TM}"),
("CryptoLocker", "{1B5MG4Hnxo5cnWKb3C8Zxt82yxdRb8AMmr}"),
("CryptoLocker", "{1B7nB4Uesa7Td5xHEko7GA7WyVdNhUCHHC}"),
("CryptoLocker", "{1B7Tjwf2VKPSUx6Cf7woKdgJ5auvzXgSew}"),
("CryptoLocker", "{1BAhkiEGZuToy5HdyFSTUMAwFwfLkX2M1w}"),
("CryptoLocker", "{1BBAoHPsTwHRTDWWyZGKoshw6FfSpEiyr2}"),
("CryptoLocker", "{1BcxSeU1ghEa2S5MCLRSbzUTEbiFkP7Tfg}"),
("CryptoLocker", "{1BF8JATdy7sB1v8XEyWH3paRnfEQKgUvZd}"),
("CryptoLocker", "{1BFguNQ3CWURg8TTqkiowF9dWbh4dgcqo4}"),
("CryptoLocker", "{1BgBX4AHDDyCNaHFUFdgn9HpUUbC4uNGuf}"),
("CryptoLocker", "{1BHkG1zMXr2bj6HMNj5kv67pD6VSE6trDP}"),
("CryptoLocker", "{1BhyNjRFU1Ywa6LFREPNetexsJyArcoA2p}"),
("CryptoLocker", "{1Bi2JWqZgZuVGgWT998v5WA9hX8zR3RKwi}"),
("CryptoLocker", "{1Bkq2Fu1VZCG9c9hgTahV6yAkC4Rmm8ypG}"),
("CryptoLocker", "{1Bp8Fya4qwBqLpzRCKaFQEdv4uui9raFĻ}"),
("CryptoLocker", "{1BPJSfJxCKYv7UN4n8UgoDYVi95don4TnD}"),
("CryptoLocker", "{1BPYyfxXXfNiA9AWQZPQd6LGhs9cUUEkv5}"),
("CryptoLocker", "{1BQqNrGCeMK6uFfQ4PrQ3bXZqzaYbpDy76}"),
("CryptoLocker", "{1BrQiciWXRtyoT3JDquQhKDKiWtYVLy2rv}"),
("CryptoLocker", "{1BtpgJP35v9QwgEtPcKvVLs6Sc2bMD5UpA}"),
("CryptoLocker", "{1BwknQgBUPGXbc7GnPrscdyoYqHieZvEAk}"),
("CryptoLocker", "{1Byox5Rh3nU4kfhQNEh7812QQDsGdBqd86}"),
("CryptoLocker", "{1C1PtDix6Qpr6SjQ6r9KvekWzSKbaVw4UF}"),
("CryptoLocker", "{1C1TCi71raLkCSq6JP5DJh2Z1cPgnuxVuC}"),
("CryptoLocker", "{1C4DD4Uzast5jLALkc22vy8TVccjbeRMkt}"),
("CryptoLocker", "{1C4dn8d9cRkbFKWHQGwXWG9kzGGViMZ2yw}"),
("CryptoLocker", "{1C4HXC1UK3b3ewKtySG2XaHz4wicE5iqwj}"),
("CryptoLocker", "{1CB5MFBw8hNLq7fnBAnYMXmCYwv8AYCpnv}"),
("CryptoLocker", "{1CBhbofpCiHeYekqqvtsD4gN7VRXyy1kV5}"),
("CryptoLocker", "{1CBtKExFSKoePhBHuuEkSfr7f6R8qYJ4ik}"),
("CryptoLocker", "{1CCetcghJh8GcDbE2BjBaJArygYCoruCHG}"),
("CryptoLocker", "{1CCuc7hReEDzPM8hbVRnKMtMncTD117yWt}"),
("CryptoLocker", "{1CcYTGyWVmoL9XX7UB4BxCFjkMucWLxjDR}"),
("CryptoLocker", "{1CFR9kkrnXEmJnXyzwkQRAryCHmKZ9XaDU}"),
("CryptoLocker", "{1CgRV3xjTSnWKj37w7zs8Tc4yc22xFygb3}"),
("CryptoLocker", "{1CKbDAqne5Y5NZkYG9uXoEoFxxwfL5f192}"),
("CryptoLocker", "{1CkwD26HPNaDBwUjQK6ZPmfpmWH1aiDHUa}"),
("CryptoLocker", "{1CLjRj6Ff6WKf8MJYc1kpbNXF2Lgr3eAer}"),
("CryptoLocker", "{1CLMS4zKMkY7NymUKZYC3LcjMw8DPqXhBU}"),
("CryptoLocker", "{1CmANXHMweSdJ7z6QGQvoskTMTByNQZXaU}"),
("CryptoLocker", "{1CmkmKftbxc27YcySVw25QzdhLG5P5y82Q}"),
("CryptoLocker", "{1CNEZs5akS2K9WhtzrBf5vKrzh8BjwNQ8}"),
("CryptoLocker", "{1CnLpveGjnL3T1a9h2N7ekZLQM9odjuxDr}"),
("CryptoLocker", "{1ComcQszPh95J8ppMzcR2cNik9CzHnxvfu}"),
("CryptoLocker", "{1CpnX11iY6vxveFJFKVUN23dBvZsHvqFwQ}"),
("CryptoLocker", "{1CRfmhUmjQRTuP4jnPPtUAffbkL6Dvfn73}"),
("CryptoLocker", "{1CrfUuTxjtjRBo9BBPbozqsUU74RHTDEua}"),
("CryptoLocker", "{1CRT39Np5hGd3mBLSyewjJ2yB9WHmNpR3P}"),
("CryptoLocker", "{1CsJoXHnkKrBhVhkf5W4jsGWrQF8Kt8hR7}"),
("CryptoLocker", "{1CTCrcgCzvVQn4KFm5AgoyeHH5fJW6ajdG}"),
("CryptoLocker", "{1CtFWbp44gvmejR93rbjjBNGogsb1vdN8R}"),
("CryptoLocker", "{1CTM19Ri6f47VqqD5JA8L2v6tytttHejqq}"),
("CryptoLocker", "{1CuMuLRpEAddm5mhpz4BfGsqp1a6Tr5KYt}"),
("CryptoLocker", "{1CuYHGqGAaKUr71NtEC3FS7fXqWePpHbKZ}"),
("CryptoLocker", "{1Cvhg97rjbPLXKCBYVU6Qt5H9eb75neQnx}"),
("CryptoLocker", "{1Cw2ZF6UjjSfa7ZVtRtRTb9riptMH9VY8e}"),
("CryptoLocker", "{1CxHPQfAbzMbXLvrewzyJS1d2j1qQ3Man3}"),
("CryptoLocker", "{1CxRjCpiXe1wW62GrKMQvE7mQ47eqYJ2UR}"),
("CryptoLocker", "{1D5NxztvrVSsBA5Yc9RCZJWQYJ5MgC7N8z}"),
("CryptoLocker", "{1D8M8Y99QNdeoDJPEXZRbAHrm2BCvkYXB8}"),
("CryptoLocker", "{1DA1aQfTd1eLuTWUGnNNjjC93MERUwR2Px}"),
("CryptoLocker", "{1DAeUa9K2unQzQP1zaGeuQbewV8spScf3r}"),
("CryptoLocker", "{1DAQ2wmgcmFoDTFJ3jUmmzQczYRmeWkeLZ}"),
("CryptoLocker", "{1Dbo8TdBobq5m7GTHNNBDFZLA8Xz6isu81}"),
("CryptoLocker", "{1DBSRad2zK8nCRB6u2ĻKHL1oa9JadinWi}"),
("CryptoLocker", "{1DCyZLjUbSCdWpqJqhx1pQhcPpz8CSA41T}"),
("CryptoLocker", "{1DDtqaaQMNf1mCztprvwSMXMg21t8GiwHg}"),
("CryptoLocker", "{1De2hmHPVfGnCTUKcmTeghnR1PX9KvxkbD}"),
("CryptoLocker", "{1DEiAbvToEZDSvsvwzTdGz6BJ6gc9Wv2PB}"),
("CryptoLocker", "{1DjLXiqJr3JSJDuELpa2GS1qtwYjfca9TD}"),
("CryptoLocker", "{1Dnyz1TqkEWGZdowH1KjdaRGQXrUWeG9Xy}"),
("CryptoLocker", "{1Dp9xaJ7vgpma9iyp9Xg4XDHZpX1qnW8RP}"),
("CryptoLocker", "{1dPwUZjv2fuWw8MCFm6UJYncQZimbHwx6}"),
("CryptoLocker", "{1DQi15kCBtSYEdGqCH5F1DTZDvpj6x1W9y}"),
("CryptoLocker", "{1DS9WQvcY6suYHpsNejLqEuwkdanXSBeiz}"),
("CryptoLocker", "{1Dsbehicbb8WmQiwMF6RMZqSh2LhrwRq82}"),
("CryptoLocker", "{1DsfbSx7miVyAztDacdDPmvMjPYKvQAHoW}"),
("CryptoLocker", "{1DsjoUo82Sr169usUW1yqUjnReŅJzLfF}"),
("CryptoLocker", "{1DtpRU49A2obVy3EHn5fAAkJrMhaLxgres}"),
("CryptoLocker", "{1DukvSVnaeGxYArhJcCVNbXZgxFjdJzNUx}"),
("CryptoLocker", "{1Dv38s2aEn3dU38DdL6MXvcUPo4ws2mnWN}"),
("CryptoLocker", "{1DvLkA7iv5BVbiSXLkT141UjYZew8xP1J1}"),
("CryptoLocker", "{1DW18Pt3qUXmojN3BjcnXkvb3TZAHKxUvZ}"),
("CryptoLocker", "{1DwwPSfWQcDcnYbJi28xSPDuEbGdKP8xVT}"),
("CryptoLocker", "{1DX4wWS6TsTS7QEBxhRDBigwUtbUUAip4i}"),
("CryptoLocker", "{1DXFJ9L8i8YTQomQnF9s7mmFwsBQ2UtDwy}"),
("CryptoLocker", "{1Dzms4coJ6BnJocxeicLykS7MnEtt6RQTV}"),
("CryptoLocker", "{1DZRHEosYzHfcARZSQJBMKWyLp9TuUhygy}"),
("CryptoLocker", "{1E4SCH63xACNgZT6uAgAzymxx4EUepDAns}"),
("CryptoLocker", "{1E4wTaMR2Tgyf9NVhQDvk5nWHYfE5Lrpgy}"),
("CryptoLocker", "{1E4ZaMnkE2XAcyuFMiMKZ8xpRCUTd55SDe}"),
("CryptoLocker", "{1E5XCdK29D1ZJ1PCwb4QMQK5ykwTyb3xex}"),
("CryptoLocker", "{1Eb71AB53EUWyq6LYWaFqcmEnXgj7wiYur}"),
("CryptoLocker", "{1EBevRx4iG9DeLiCEQJd3xiVLvnFfEYLvx}"),
("CryptoLocker", "{1EbSeUyF3DTYMgApySovqCi2JvPRqy5S42}"),
("CryptoLocker", "{1ECe37xTBbyXxZ5DhxYbw85tJttQ7zy8pd}"),
("CryptoLocker", "{1ECKTr4hU5Cr5ix7JCGKNHRgeBhP3nSSui}"),
("CryptoLocker", "{1ECtyMAbqHhsmyWqvPVE6cbPjNDUgwh7f2}"),
("CryptoLocker", "{1EddPDLLdVn28FquRCaiV1Gk9DKPxyTRnU}"),
("CryptoLocker", "{1EDhbqWc1u8HEQn9SRzx4dK2CC4RTubcEW}"),
("CryptoLocker", "{1Ee6kAYWizdFG7bxiHY8W3NsgbCceU91U7}"),
("CryptoLocker", "{1EeDUzBfberCNb5AoSn1FKmZAiqegB882H}"),
("CryptoLocker", "{1EEVmrKkLP7LmBVrrX9YAP3JwKVh31Covx}"),
("CryptoLocker", "{1EeYeuEPWv3xWKGZ41QeLX515TkmQv5iA3}"),
("CryptoLocker", "{1EHjcjsP17zPNHMWT9tAZ5X39AZUTVGwEQ}"),
("CryptoLocker", "{1EjoQZ5Rzoc3ZiJcP2doHxDh3NyZpGdhx5}"),
("CryptoLocker", "{1EKdXWq2RZqPKyfgY2FhzVsNGhkJB3aScJ}"),
("CryptoLocker", "{1EmNKwkfxfVBiEJo8qRa7UfZTLXERYJm7E}"),
("CryptoLocker", "{1EoQAfpDVHAgbkNqAh4odyuaiyAo3csU5X}"),
("CryptoLocker", "{1ETnvsNw3P3RA1NK9x6KCAF74c8PFmd3EB}"),
("CryptoLocker", "{1EyNoagZyGi9G4ntcgyT3rZa2vy3qyTpVn}"),
("CryptoLocker", "{1EzoppkGf1MTB6U3NsaypS2Dq916EuTzn6}"),
("CryptoLocker", "{1F17BAk9BAT4GFzc1L1pc8R1dyDGepqb78}"),
("CryptoLocker", "{1F1maĻkZaAhLjftMEhQjWKHXmTVN2q9Vb}"),
("CryptoLocker", "{1F4VDvqfJuBZqdmzyagMhhZSoqYLJSfWmb}"),
("CryptoLocker", "{1F5USwafW7bbgzPU7xcnaV7FJztBgGLzt7}"),
("CryptoLocker", "{1F9W8EmdJsQ67FygDAFxcKtefQqYCbyivN}"),
("CryptoLocker", "{1FC8MEbXZsgmG8q86DQTwNy1hsTiwSCTmy}"),
("CryptoLocker", "{1FCALPEBJjwxUJmPQHohabMCEgGuXZ6B6j}"),
("CryptoLocker", "{1FcomMQ1nKoVM5RfSzrB4uUSxMKWKhNB13}"),
("CryptoLocker", "{1FdWKDQg6Zqh4s6Zc89hyAbRBAeNRUwiAz}"),
("CryptoLocker", "{1FE32xcN5mPetyPqAcJSzSjgVeL84jEkWP}"),
("CryptoLocker", "{1Fe5bXZdDyfQwJsfqGNTZn5b9SLd7etatT}"),
("CryptoLocker", "{1FeXvY7QRKXJxCgTRgtvnDUWy7bpQCpgP1}"),
("CryptoLocker", "{1FfpF7bcUNghqvWJfV8D7yeSwYMxKAgLSi}"),
("CryptoLocker", "{1FiJZbEPxEhyejt9AYUyZqhVrAz7zmD8S7}"),
("CryptoLocker", "{1FiqZ8kvve4H6kNM377wZoVM4vZv7rUrVf}"),
("CryptoLocker", "{1FJ5ka9LhzYXtnWxR2Eg5Vz3J68dqRVfnf}"),
("CryptoLocker", "{1Fj8QatpHtV3FuqxhUEeYK8kmGcSN3EN3n}"),
("CryptoLocker", "{1fkey8rWjxhxgSaKscEfSHrENqz595nGq}"),
("CryptoLocker", "{1FKyUwJicY5GQWFzFBsoq6ZbPUA1iH8dbY}"),
("CryptoLocker", "{1FL7yXdVaAFPew6yFHoCBUXwmf1prHUNRo}"),
("CryptoLocker", "{1FLUcg2mLX5JEe5uePUJfrUPwzTrBceUae}"),
("CryptoLocker", "{1Fo5MU4CLnXnEhSSPgbP4J7B9ZDsa7NDuU}"),
("CryptoLocker", "{1FomF7gPpjvmhySGWNuG33zAPq2sBCymLu}"),
("CryptoLocker", "{1FPbuf6qGM4UvmyVLwZ42eXmXqTZTChWje}"),
("CryptoLocker", "{1Fpg89LmrdWvCtkTZF4eTccCdoCrSqniGh}"),
("CryptoLocker", "{1FpnnbNyRYULiCqUWTBnpb2r9ke834EjLZ}"),
("CryptoLocker", "{1FpwqB5eR3v1rNvBHhG25uneZVe4CBNqp6}"),
("CryptoLocker", "{1fpYNCzkX1jnmkDXVidvLH3iEsJqS1w7B}"),
("CryptoLocker", "{1Frx1yLmQDfwW7qwbjzJcmhSLV5UXbDqHw}"),
("CryptoLocker", "{1Fu4ZtjsiwoWWkrzFKeLcLzMCoWb7h8iXf}"),
("CryptoLocker", "{1FuSaQCKCP9FzvTToUXzJVfkBHvksTtmP5}"),
("CryptoLocker", "{1FWMMeLcSEowNZQqihvtNUA3QnMXTWdjkA}"),
("CryptoLocker", "{1FXAevqm1uTVjZYsMP1H4qsUsM6jkXmr6S}"),
("CryptoLocker", "{1FXjExYfwrevdn2ebud7gqzGoAnsER2j1W}"),
("CryptoLocker", "{1FXUajdhKTjknLVq1VK38CcsdxLgytJM2E}"),
("CryptoLocker", "{1FXuyMgajuYWJHYNgY43bhMFjw8vprBUTD}"),
("CryptoLocker", "{1FYuKG14vqGKpyQJDEw8id3n8JysYJuETq}"),
("CryptoLocker", "{1Fz1vD25Lfz2f8etMAEf3Lfo61YjBFKGxN}"),
("CryptoLocker", "{1G78aJr3UJSJCa9Kvo6UgR256mMZ2BC2xe}"),
("CryptoLocker", "{1G7QEjGLsZhJmwCoWjGc2pYXUNMtAquSes}"),
("CryptoLocker", "{1G83cC32bsj7EoqTcW7w5gyZGVdztqAD75}"),
("CryptoLocker", "{1G9s21qsoiMMp8xYXBRiDdiXH654pSgzsP}"),
("CryptoLocker", "{1Ga2126kt8LjDydfnkiHsVeraoza2bFKou}"),
("CryptoLocker", "{1GABxutuqRpC2vLwTaQ8s7TNPWSBVVLsP5}"),
("CryptoLocker", "{1GARaX1nng9n8SboqvAgABj7UG4tGoCRD8}"),
("CryptoLocker", "{1GbvmuaQTbPiLYhFxjowiRPy4BR6UeEKhS}"),
("CryptoLocker", "{1GDhTJf2NYNvs7Cf3zFhGtvp8PFArC4UnV}"),
("CryptoLocker", "{1GecZMfEtpWgEZaWXyGyehmuzXKCW9DhGL}"),
("CryptoLocker", "{1GET3HUUucNJbynop4V3YXDsXNw1gW2jKB}"),
("CryptoLocker", "{1GfYHVDVvb2g6a3mK1xDTPB3CLiZ8Gs6Jz}"),
("CryptoLocker", "{1GGJk8DjqMSQSiGbtfJiYv8d63pq1m63Mb}"),
("CryptoLocker", "{1GGm1pDwN4qp3AyWfP5hsswFWiXm3TcHcw}"),
("CryptoLocker", "{1Gh2SQa7v2mMH749TNSmzi1MreC2aRQdjo}"),
("CryptoLocker", "{1GKKgv1ti6KyCwTR7qeUAzGJMgpM5CQG25}"),
("CryptoLocker", "{1GkYLJ2Vsq5qXpZYGuqjDpknNiin3tiukT}"),
("CryptoLocker", "{1GLWrDUYcLxFpxsbRXVQgkP2ooXUt1oNvs}"),
("CryptoLocker", "{1GmwPizjg7a6LTdpr9mAYqDxxGEZ6Łau}"),
("CryptoLocker", "{1GnBAQXrjsJPtLD41p4nMKWGiJvhtGDwST}"),
("CryptoLocker", "{1GovQ81qq8JyBAzcqK8T6CSUxpHf7BuFF8}"),
("CryptoLocker", "{1GQweUgXZAtzbgrTYCBveUmtmL9d8VTiRn}"),
("CryptoLocker", "{1GRwhW2XH2bv9bGFCGDcaL5jkZgxp3Zdwk}"),
("CryptoLocker", "{1Gry7e5ACm6iXr3Gn8idgZ1x5Q8L43kkA7}"),
("CryptoLocker", "{1GTTj2YiHZc1JF6wdY956QHTAKQDYCdwNA}"),
("CryptoLocker", "{1GTxj5VvnMDqHvGTdzyxSP2n2cGQBELZ6c}"),
("CryptoLocker", "{1GwRMGzPdKmaLmwB2Gy8mm6YFAMvjrDnBs}"),
("CryptoLocker", "{1GxSaCJxDoSA2rS1fUp9ozQgsV9U64UGSb}"),
("CryptoLocker", "{1GY2zSQN33EUdSx5GYosFFNoz5XmiaKun4}"),
("CryptoLocker", "{1GZQuQnPzBr5thipDapTBGqLcQW1Ruz2nr}"),
("CryptoLocker", "{1H2rvLTArfMTNsWK7U4tNqvN9V5K5cDZaF}"),
("CryptoLocker", "{1Haq892MR5i9tHymDBAa13yFaGqsorMjYK}"),
("CryptoLocker", "{1HASDyP1vzmh5Va7vdE9bJtKAs7RLnPhq1}"),
("CryptoLocker", "{1HB6hmcTeCLjeLBcafLx5ZpMTbXKWMn9m4}"),
("CryptoLocker", "{1HBwtE3ijt7ceMJJZ3ELfsUQ5scLTcguiC}"),
("CryptoLocker", "{1Hd5zaKwM7PMTTb3kPeDdyeJK4jnnhSHqH}"),
("CryptoLocker", "{1Hd6C1xyNmtJMQ89BpNGFEZwkiPH54nbCN}"),
("CryptoLocker", "{1HEJo6qYfSxnFmCtGE7AzCE4hgBYPrdbt9}"),
("CryptoLocker", "{1HEYE618LxL37EFSW1WKUoWkNMhPo2Hd3V}"),
("CryptoLocker", "{1HFZF6PwHcuremLVrwAUznKRr1VpmTqBrz}"),
("CryptoLocker", "{1HGhCRoGbsvXm2dWrvN9PHnHcuLtKhvKyL}"),
("CryptoLocker", "{1HJefw65SQi2HeWys3B4ZsinbpLifmNSbs}"),
("CryptoLocker", "{1HL71LjsvspbhhKhWUyAV9XHt37s3zoy33}"),
("CryptoLocker", "{1HmtPJehWEHoVm5im1sRrEh4ivfFtSLwKC}"),
("CryptoLocker", "{1HNiqcy59y3toikWoECd73XP8DJxGJ2sxU}"),
("CryptoLocker", "{1HNVA4DQj3e31SxQwcYqUQJdKEaU7h3MYM}"),
("CryptoLocker", "{1HodueGYwUi71qŀj7Hyf5kcq27CG7LqRW}"),
("CryptoLocker", "{1Hojtyssp7qz7RwT4eKRYtP2nrZ96H3qZg}"),
("CryptoLocker", "{1Hpb5KmRWwseh33e5Jd3Pn7ULSsDix19N2}"),
("CryptoLocker", "{1HRFyimD3gw1cZeujv16qbDdkVZzvWUHJ7}"),
("CryptoLocker", "{1HrYXbD156rr4NXNXUQXcQRUrpsZsdK5Jt}"),
("CryptoLocker", "{1HsuVCVJaxqiv7UsPoG4HvHCmDAhtHtE9R}"),
("CryptoLocker", "{1HuXuo6KaUZUwRS5apbcDZz94MMgaoEBfG}"),
("CryptoLocker", "{1HuY8s3zB6msu3Kv7R9TtVctReyVbyuicQ}"),
("CryptoLocker", "{1HwbWAqoqL5cytfZRCoaD3P4WwtdeVjjs5}"),
("CryptoLocker", "{1HwF7DXbxxzvRP7U5uRqVh1jw2wzjt9mrP}"),
("CryptoLocker", "{1Hx2Mz59JgTELeAgwpyMKpc35MqY5Rm5B4}"),
("CryptoLocker", "{1HXEb9sDcGmLR29bMbTeFnvHPdeSrKZBsu}"),
("CryptoLocker", "{1Hy3yJDW23FBXdco1edFv8xJ6Q7w9po4bR}"),
("CryptoLocker", "{1HYHeBkQ8fgPFEtKVkCjCouZjwEbTm2HMW}"),
("CryptoLocker", "{1HyqQaTQmUT3FpnNMCBcpjBFv9BS7dnbZL}"),
("CryptoLocker", "{1HYsM31uGzShVJzkAbVbCTwyCRNzk7xAcG}"),
("CryptoLocker", "{1HYtcZdXwi2YRBr7JcqB5BCej7Zuts9Lph}"),
("CryptoLocker", "{1HZFJBtpPXmpxi8v4iSrWRydkPfP77QvBT}"),
("CryptoLocker", "{1HZHtdkkrCgTpXzNah1s825SedX1zFj7C8}"),
("CryptoLocker", "{1iPwrjCp5761ofo5xbAf89xaaSWZZAP7K}"),
("CryptoLocker", "{1J2vjU43q7Ga9moLvMz6vHEZtFGx5cTien}"),
("CryptoLocker", "{1J6D7xqYBUj49atDRPVBCA3L22NL6pviZJ}"),
("CryptoLocker", "{1J7GbwwtEtoXhrmCwBtSuuT4yZTxwdn8Rw}"),
("CryptoLocker", "{1J8kNsXiDkoaHH54UidfhQya3ztH4CVWhF}"),
("CryptoLocker", "{1Ja8vLupptShzK58xaVp8yq3zJzzCEEAjj}"),
("CryptoLocker", "{1JAdHKLyhUuE1W1Fwkqeseq5ybnzwFvM7w}"),
("CryptoLocker", "{1Jbe1c8Z3k6FeF4q3FhMzjgsqxh91SxRXo}"),
("CryptoLocker", "{1Jbrbj66UWqNLxoMcmxxtnDPA9oDTRzKej}"),
("CryptoLocker", "{1JDaYbuQD7PWvKhM3y9UbpemkHc4kybFxP}"),
("CryptoLocker", "{1JdJ3qFFBnXbMSyuxFosniB63RxxBGY6aj}"),
("CryptoLocker", "{1JF5wnR7mPLR2Cga3LUQ5XDwv6XuBC6vDr}"),
("CryptoLocker", "{1JgAxMvC4RjEtRLADCEH6YCFiBEyZgg77H}"),
("CryptoLocker", "{1JKJByjqRibD5oYbLujEc3hwSgQmWb1FNU}"),
("CryptoLocker", "{1JLHc1VFhGo1KtKYnpBJmRwCimuSj1baHj}"),
("CryptoLocker", "{1JM1NifNYWFyJBbw6DRAmXgzfWCvihvoHp}"),
("CryptoLocker", "{1JMhN3hHoYotkKNGEFRytc9JgiGCaHqYUf}"),
("CryptoLocker", "{1JNrW6qR2KC9WQT5KHd7aaWKbmUCWp16dq}"),
("CryptoLocker", "{1JowuBwNg3GnhNTDny4zcE2rZtm2khL2oJ}"),
("CryptoLocker", "{1JpGVe6bGeeLZZN6kno7Mwt3KXesWdu3Tx}"),
("CryptoLocker", "{1JpzmkutgYkHZ8kAprdW3BVSVgLitE7J7G}"),
("CryptoLocker", "{1JqrSXoWKcjpSr1bfzu7Gbyiyho3CYVKGu}"),
("CryptoLocker", "{1JRZwX3cmQxtDvCvmoova2j2ZXaX4V8Ekq}"),
("CryptoLocker", "{1Js77if7NxxyE2w4UahAZv1fsa3emq2Pb7}"),
("CryptoLocker", "{1JsMYv9rEe1FCDfejvJyviRqnGf9pStZ6e}"),
("CryptoLocker", "{1JsXconekGH1YyQLBT4GeCEv6KLWBUHBsE}"),
("CryptoLocker", "{1JukPadus2HCjmXWpMyac9Coobzd3AMSnt}"),
("CryptoLocker", "{1JwoR6p8sBmh62GKm16ZWChP63CVAESUSo}"),
("CryptoLocker", "{1JWZ4gTAFcWYj7W4qq5XbpPKEvPSghnnda}"),
("CryptoLocker", "{1JYJTrqDfZK9jXUwrQ9zb5EndTWRDAHKis}"),
("CryptoLocker", "{1JyxrkELCNMECqLkE57jEFMX5otmGgcBSZ}"),
("CryptoLocker", "{1JzbvZ8zvWecnf6k7rp5isW9QQfpHpXZrT}"),
("CryptoLocker", "{1Jzy4sUHKjCDxykXDuFCztPH27MAf59Tng}"),
("CryptoLocker", "{1K2SbvQSWtRaiUdvvPKNnuXhwm9wuHnKFx}"),
("CryptoLocker", "{1K4qn9vJFVMeiZ72cVbnn1QzzAJEPtrre2}"),
("CryptoLocker", "{1K5jNeFdq5KWdyPuPBpodmgzGJaGmVExhm}"),
("CryptoLocker", "{1K7SGqhu5qzWQsSxP8Z8K2VCUTR4AphaU8}"),
("CryptoLocker", "{1K8Mz2g1FzNEUrZPDe4ffXJ5kqKnUYqG4L}"),
("CryptoLocker", "{1KAeAQ3tWeQ3B7sQak9dWed4nj76HVaari}"),
("CryptoLocker", "{1KaL5hmQ8apJWP67WhsJ1685cdtQjerc5w}"),
("CryptoLocker", "{1KarHM9eLhGjNAxN28dP6EHPG6CoVHPG4C}"),
("CryptoLocker", "{1KcCWoyq7YzBXsgoiEenXS7EtksmYFrEqb}"),
("CryptoLocker", "{1KfPJQ9HSod2WUwUzr4MK4ySpNnnWK9oAy}"),
("CryptoLocker", "{1KFqey6J9NP2vvUCpgEuvk22iFysVqCSPt}"),
("CryptoLocker", "{1KfY1pzLRHDPHzGjzqHyfrKmy3Z2Z4ifeW}"),
("CryptoLocker", "{1KHB1T89jBPGseWDb7DFug5JLktspRkasc}"),
("CryptoLocker", "{1KHDAbeA4YWdMHCj3he4sAzJXarWX7HFht}"),
("CryptoLocker", "{1KiJiDFptxgogXb6iMfvsQ3bdfaZrUXuFs}"),
("CryptoLocker", "{1Kmku9uQ2ckxwFUnhqPTHdKqiJN57qBRTq}"),
("CryptoLocker", "{1KMSt9JPspuszaNEaGA85afwhviG3hPsCE}"),
("CryptoLocker", "{1KMYBUHzLwgJCXU6NotVUFDkaCQHnMD7Ek}"),
("CryptoLocker", "{1KvqosZ3B996QwPg3w51eCZvorj5gVUFet}"),
("CryptoLocker", "{1KVy4ADmBt52JgQnrKEui7D9Ka2J6XsXir}"),
("CryptoLocker", "{1KzHfDDpPs9f11bXTU9yxu67E3Yrh5THZE}"),
("CryptoLocker", "{1L4ypAukN5qmh4B8f6jVE5xu7yiUBveGrf}"),
("CryptoLocker", "{1L7WevwfDj7AzEXŀ9j1BbAYyCZhjKLvBv}"),
("CryptoLocker", "{1LaNYWVS5dq3FvXvA8PUK6hByQrGR7BTzQ}"),
("CryptoLocker", "{1LB6eL1iGZsJVWfhDLASmU2L9o8PhEzcc7}"),
("CryptoLocker", "{1LbJLb2kgqA2aZo5radKYsQP27wi7oNpis}"),
("CryptoLocker", "{1LbsNHA5yTtUPmDozodEpvRQpBRNtuGuhu}"),
("CryptoLocker", "{1LbwzyzGVGDzZ12r3fenoZwKfaKKR5SoUq}"),
("CryptoLocker", "{1LDKfUJGBEbso4gdbnJQ3HBm78H7hd1j6g}"),
("CryptoLocker", "{1LGfYq32BKZosBjaoK5isnXSj93VY3kBXJ}"),
("CryptoLocker", "{1LJusykjpqSbBU4LJwaRak41SHdPqyjf1a}"),
("CryptoLocker", "{1LK4EgUYGm5uE3xUWNr16nmYoYChn7TwQf}"),
("CryptoLocker", "{1Lm23UB6oaiAazN5toNp18N2EuVSvFwm78}"),
("CryptoLocker", "{1LMLFYedHSYBYDf6B1QEi1eM3gCHHB96gt}"),
("CryptoLocker", "{1LmoZhPL7DXoNW82LmmzKTw1vEsoKUjK3F}"),
("CryptoLocker", "{1LMRafbNmzG9H5g3GaAWjhX7wSnCXGNHMs}"),
("CryptoLocker", "{1Lr5vY8SD8wC5UoGmFW1MiWcXSzFj7sKcj}"),
("CryptoLocker", "{1Lr9HrVPMqE6hBUor4F9bQn4oevazwGGLT}"),
("CryptoLocker", "{1LxiKcy5SH5RtPCqkEVeotw2JX5r5VLYYG}"),
("CryptoLocker", "{1LysaXN5BbfL1FQcQRNLVs2KFPoNhgeNZ9}"),
("CryptoLocker", "{1LYVUUwkikgc6mApuqaeVfHvE2y687J43Z}"),
("CryptoLocker", "{1LYWiERCo6m7fEykJ4uuHuCtmeokV4haPy}"),
("CryptoLocker", "{1LZRPrhDh3b9EAs26jKhG2tmYNrJ9TC2cw}"),
("CryptoLocker", "{1M2JeWxEsgPK643KP3Y23RAHVKzxMoXtwY}"),
("CryptoLocker", "{1M3oQzqk7TiPbxY8CqWNBd2czMa8NpgVxF}"),
("CryptoLocker", "{1M4A5HRARXyfMQgDWjSzQCpKqkMuCAHQdU}"),
("CryptoLocker", "{1M5GHvYVAtwJ9aZi2GWiNpNQwcimYReXqs}"),
("CryptoLocker", "{1M83NXYuPpjEjYt8baXYxriQNCDyfWU8i3}"),
("CryptoLocker", "{1M8D6Z3sCPCBmSbNDqcu4DHtTsSQw4B6QF}"),
("CryptoLocker", "{1MBhjH2kLBFoYLLucjKeuVhRhfuWZRGX2G}"),
("CryptoLocker", "{1MCJg35DzCuxd6JTQiDEhK2m5rVRcgFwxj}"),
("CryptoLocker", "{1MCPK5yQZnPBCYFDLsDtuHxxqDyAqoYtMG}"),
("CryptoLocker", "{1MDf3iwRESRcNAvcJqbMf7VXVoejUUzn6T}"),
("CryptoLocker", "{1MdtKc62oYkhjLvLufGcXvMz1KaYrp6p7D}"),
("CryptoLocker", "{1MEVHp2divX2HUKzYY3bFRcM7fWtn3XREp}"),
("CryptoLocker", "{1MFFrqqbHSva1VTXoLN9dVVgqS2kNVmjUY}"),
("CryptoLocker", "{1MG5UCeoUbQyGteP4ze1CYCbSuR9t9y7oa}"),
("CryptoLocker", "{1MHmXHCQiJ578GXCuUosvTbwa3CH8wjqgj}"),
("CryptoLocker", "{1MoWyT8Perux324bpWDhBdh1eTtvR8gw72}"),
("CryptoLocker", "{1MPXvqtB923bhA22ru11TacfCagY3Chwhi}"),
("CryptoLocker", "{1MRss2agjSaScZg2wHEvURncwxKvQE44BC}"),
("CryptoLocker", "{1MS4zNjpMKqNmPC3Mq1gAp2tYNQB3bbrmP}"),
("CryptoLocker", "{1MSFtiA8DfnpxGJsUg7adc4q5T9rSFDnvC}"),
("CryptoLocker", "{1MUi4DPgYpknfPnYy6TtLPA4R3H4dYJEau}"),
("CryptoLocker", "{1MwCwmKezKDkVeJo6XB3wgZAoxebWpRXCb}"),
("CryptoLocker", "{1My2nTaiG7bbtBdjUUw8huReufaoVRwLeT}"),
("CryptoLocker", "{1Mz8NVbYkWY1c1U8CsFKSZHa8VAzU6Fjd2}"),
("CryptoLocker", "{1N2bGYAqcZJ1nBDpFo2rhGq4kSCWDYgKsG}"),
("CryptoLocker", "{1N4hjEZd2KEz6d2Cd8JVCfJFvNJAQDtX8P}"),
("CryptoLocker", "{1N53V8f8NQPyHUHqj6e4mHCqrJsgsYMdq6}"),
("CryptoLocker", "{1N9DCCacu84xok24gJcYoj2hWmfEa9gHxD}"),
("CryptoLocker", "{1Nb7ULYgbkLH8Agx8AopVMVAEjnXHzTBSm}"),
("CryptoLocker", "{1NBGp3YUVwMFc3vKvHE2XLD8S3uzPN9Ujm}"),
("CryptoLocker", "{1NcSH3gPBpbLeUE4WZGHu5JVWnh6JPwaJv}"),
("CryptoLocker", "{1NE4Do4DZg9GuzRq7sruvvnTgpG27xGDWx}"),
("CryptoLocker", "{1NEc9b7C1cYXXHJgpVNfzJFPrYGhDWzFn7}"),
("CryptoLocker", "{1NeiNq9hZxAS75HMfzYFzxnYDpSGVEPDuY}"),
("CryptoLocker", "{1Ni2d6ntLrza9646HmdSgymseAibe8oyKG}"),
("CryptoLocker", "{1NkXbJhkb6any5KSN9jhaEw75xpxo4FM5C}"),
("CryptoLocker", "{1NmDnGWpGysUaKRYC7QWqwYPjuNsGNa6wx}"),
("CryptoLocker", "{1NMiiPNGb93VwTXGrJY8FPvnLnq2ZSdct1}"),
("CryptoLocker", "{1NmnkzfqEtaLCSjVAH3sc2cXgAREz21dk1}"),
("CryptoLocker", "{1NMVXwfpZpvG8DECiv1KjFKvuUzVLS8FQ2}"),
("CryptoLocker", "{1NNn5Ybnk9DCiJR5ypVdrvf7WVxcqFWCc2}"),
("CryptoLocker", "{1NPqptRx484Ntyt8SuZRPKVsekJHeYjTme}"),
("CryptoLocker", "{1NPZWdqghuS1SufK25wJWKpb8Ub7BbC95}"),
("CryptoLocker", "{1Nq6H2kb95nAfZ7oH8NRm9nen11VG59tpa}"),
("CryptoLocker", "{1NqoLabo31gu3fopiTrWA3UVXkCVEmb9E7}"),
("CryptoLocker", "{1Nsrh7pZpz1ueSQWdRm7JHnw3Trkv79eZa}"),
("CryptoLocker", "{1Ntp9y37tWGpibxUs4jSzLkMnT4GRn9Txv}"),
("CryptoLocker", "{1NTqaT3skX1ButcNrDX9qxo9V8Z3CzdPXh}"),
("CryptoLocker", "{1NXVijH9dvaLD93UkJ2xxyd76831mXX4zg}"),
("CryptoLocker", "{1Nz3P4pRebLsaJft83qYMTRvVsurN3BGKw}"),
("CryptoLocker", "{1NZe4jSrKsjLHbYUpxSvXqVtKvesbHVu7F}"),
("CryptoLocker", "{1NZnh3sWpHWrP5TjaSJFHYLBN9jNL61RRa}"),
("CryptoLocker", "{1NzQGLLzxWumfnP2he2bQhdmxu4ka5i9Uj}"),
("CryptoLocker", "{1P1yJN9BkUEPhqQ4s6umLJiSnKJfrff7XH}"),
("CryptoLocker", "{1P4wU3HaZT2ANajv4cnC2VD8t1WVUd4eo9}"),
("CryptoLocker", "{1P52Hgptcnre8ejGz6GtHsdwLQP9fPP3M9}"),
("CryptoLocker", "{1P6GDJ7NwsDygz6tyKPDGEuh4GRA6aXrgQ}"),
("CryptoLocker", "{1PARbtVpdQ2DpkMHRvYkkkuSGgSDVpFrSF}"),
("CryptoLocker", "{1PbnSDMsTJCvtWkz5vxyC618sjA7rQzpp3}"),
("CryptoLocker", "{1PBZGajyhi8M9436Amt4SYLf9r2BMHCEPA}"),
("CryptoLocker", "{1Pc4r1wBWRXmJiEBQWPrw9pTdF5srESZLP}"),
("CryptoLocker", "{1PcM4ytXhzSubRCLvVm21BG3os7gCjDAEy}"),
("CryptoLocker", "{1PcMY64nst8n8vLjkBkPGPdp9ADvKhYuMk}"),
("CryptoLocker", "{1PDEFwCuYBt1t2hny85Bvac5bJTZsJaftr}"),
("CryptoLocker", "{1PejiRQcz65paGK9vs5iu1iA4qoXQCscQy}"),
("CryptoLocker", "{1Pf7ZDS6hmHNZHHkL9WRmRWoLpqJ1TeVTe}"),
("CryptoLocker", "{1PFdNRASdBV3sLEmZRgxR8ZC1U1s36Zjkc}"),
("CryptoLocker", "{1PFr6CqUrkjfdmeMm5HpUdwDYAAT6NJpi2}"),
("CryptoLocker", "{1PgE9vt9sfCyFj4rmL8uL9WqM2BqMXeE25}"),
("CryptoLocker", "{1PgttDwaoBn9fT1yCSS9SqW2y7XvutQhoD}"),
("CryptoLocker", "{1PHq99kxUC9PNd8jYfvZ6K5ov3oGw3LfpV}"),
("CryptoLocker", "{1PLqvqitTbafqdmfMPPT8nKaJqJTVPxZms}"),
("CryptoLocker", "{1PMeia8xa4c3CnoAw3C2EPsXbeoTSpPm89}"),
("CryptoLocker", "{1Ps7VKoKheZggkT49aREmxmaDzCDSS2jJJ}"),
("CryptoLocker", "{1Psk3JsaE97rubL9mu9GqM4S9yj1hmhtQe}"),
("CryptoLocker", "{1PuCT8f5Wk9MhBN3xPBE2r3BArkjyF24vP}"),
("CryptoLocker", "{1PUHb4rzzVw82vGWrTx2esTqWV34qsiLoM}"),
("CryptoLocker", "{1Pv5FYte855Ew8RDTeaqhKXFipJ28DrPte}"),
("CryptoLocker", "{1PVa2LdducsHXWgtAUMCrHRUhNyEgQYWNU}"),
("CryptoLocker", "{1PVTFqz6ZeuwqCKnEULF2tYUBoNocNFczH}"),
("CryptoLocker", "{1PwsadYujh5RQMpp7QCedWshvbhkEMbrA3}"),
("CryptoLocker", "{1PwVZy5UBwvJ8KmmS9pe2AuQTv4nyjHywe}"),
("CryptoLocker", "{1PxJj8iogRk8ELea4t8mobNt2LinZGZonw}"),
("CryptoLocker", "{1Q2A1uvYqNNWgVSKiZSdMYb9MrVeTSdiXK}"),
("CryptoLocker", "{1Q3BJuu8t99i23wNF4pk8rLsKwLWWT9xvn}"),
("CryptoLocker", "{1Q4LkTm1MRR88jRUNSnegGyYi8MpqKMGMC}"),
("CryptoLocker", "{1Q62f7bkkd3FUg7RrrEPwPmEZZGQhspNGF}"),
("CryptoLocker", "{1Q6T6hGvRUZBzWtMB38kE1qxw2N9RoRBnY}"),
("CryptoLocker", "{1QA3Ho9g5b2U31gBCQzo4b5tnJVmM8R5Kv}"),
("CryptoLocker", "{1QCttxVQLvJMGGR1LFsRNxHfQeNWvH6zW}"),
("CryptoLocker", "{1QDSsu2oNhj8BcVXrgTXCXgYkHZ7kH9qx9}"),
("CryptoLocker", "{1QH52jLJDaD8zYeaZDM4M687z4FpdX7CZZ}"),
("CryptoLocker", "{1QJCsWZ8CCrwECfyxxfcegrvNAbJbg9Zks}"),
("CryptoLocker", "{1tP8PaTzUCyCfVahS71jV58ubtNn7zfhH}"),
("CryptoLocker", "{1UCJ8wot2HqfEEBebANG36FTVMpwAE44r}"),
("CryptoLocker", "{1v1hLeFZczCBQeuDRyKwjXrs5pKWqp6kB}"),
("CryptoLocker", "{1vtbjSigZsfg9APnoANQUpG3nsYczRPy1}"),
("CryptoLocker", "{1WbtRF7ZsqQjQPSXY2usAKJCBjDikDbCg}"),
("CryptoLocker", "{1WcrmM6iAHyoZWriGoMW2AZtFDSL8gYAU}"),
("CryptoLocker", "{1WWPwBRyUxs9qCKmf1QWQU1wsZbk8bGJg}"),
("CryptoLocker", "{1ZeekMuuqzDD3KNXe1Tg5pRugKEHsydWh}"),
("CryptoLocker", "{1KP72fBmh3XBRfuJDMn53APaqM6iMRspCh}"),
("CryptoLocker", "{1AEoiHY23fbBn8QiJ5y6oAjrhRY1Fb85uc}")
)).toDF("ransomware", "pubkey")
ransomware.registerTempTable("ransomware")

// Joining both tables to get the transactions with the selected pubkeys
val table2 = sqlContext.sql("select a.* from table1 a, ransomware b where a.pubkey=b.pubkey")

//writing the results to HDFS
table2.rdd.coalesce(1).saveAsTextFile("ransomware_result")
