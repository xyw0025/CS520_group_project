import { Metadata } from 'next';
import Main from '../components/Main';

export const metadata: Metadata = {
  title: 'UMassenger | Connect Students Together',
  applicationName: 'UMassenger',
  description: 'A project for Connecting students together',
};

export default function Home() {
  return (
    <div>
      <Main />
    </div>
  );
}
