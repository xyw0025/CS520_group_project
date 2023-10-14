import Head from 'next/head';
import Main from '../components/Main';

export default function Home() {
  return (
    <div>
      <Head>
        <title>UMassenger | Connect Students Together</title>
        <meta
          name="UMassenger"
          content="A project for Connecting students together"
        />
        <link rel="icon" href="/fav.png" />
      </Head>
      <Main />
    </div>
  );
}
