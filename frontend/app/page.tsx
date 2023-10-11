import Head from 'next/head';
import Main from '../components/Main';

export default function Home() {
  return (
    <div>
      <Head>
        <title>Po | MSCS Student and Full-Stack Developer</title>
        <meta
          name="description"
          content="I’m a MSCS Student specializing in building web applications."
        />
        <link rel="icon" href="/fav.png" />
      </Head>
      <Main />
    </div>
  );
}
