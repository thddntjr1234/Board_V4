import { Inter } from 'next/font/google'
import Head from 'next/head'
import Image from 'next/image'

import Navbar from "@/components/Navbar";
import styles from '@/styles/Home.module.css'


const inter = Inter({ subsets: ['latin'] })

// 메인 페이지
export default function Home() {
  return (
    <>
      <Head>
        <title>Next.js Board</title>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Navbar></Navbar>
    </>
  )
}
