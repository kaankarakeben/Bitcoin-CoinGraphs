# Bitcoin-CoinGraphs

E-Commerce is increasingly becoming a key part of the global economy and our daily lives. Prior to the invention of cryptocurrencies, e-commerce relied almost exclusively on financial institutions serving as trusted third parties to process electronic payments. This conventional e-commerce system suffers from the inherent weaknesses of the trust based model which include a lack of transparency, fraud and high operational costs. Bitcoin was invented to address these weaknesses and is the first decentralized digital currency as it works without a central bank or single administrator. This project focuses on Bitcoin, but there exist many cryptocurrencies now.  

Bitcoin transaction are peer-to-peer - they are sent directly from one address / wallet to another. These transactions are verified by network nodes through cryptography and recorded in a public distributed ledger called a blockchain (proof-of-work) to prevent double-spending. 

Mining is the process of adding transaction records to Bitcoin's public ledger of past transactions. This involves solving complex mathematical problems and was intentionally designed to be computationally-intensive. Mining is also the mechanism used to introduce Bitcoins into the system: Miners are paid any transaction fees as well as a "subsidy" of newly created coins. Hence mining serves the purpose of disseminating new coins in a decentralized manner as well as motivating people to provide security for the system. 

In this project, we have used Spark and Scala. This report will detail the following: extracting transactions from the raw bitcoin files; creating the vertices and edges; and building a directed graph model to detect and extract triangles. 

Our additional project goals are as follows: 

I.	Visualization of the bitcoin data (for 5 hours from 2013/01/01 00.00 to 04.59) 
II.	Analysis of Wikipedia top 30 donors, total donations and graph for the data period
III.	Analysis of CryptoLocker ransomware bitcoin address
