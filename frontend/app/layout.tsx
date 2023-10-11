import './globals.css';
import type { Metadata } from 'next';
import { Open_Sans } from 'next/font/google';
import Navbar from '../components/Navbar';

const font = Open_Sans({ subsets: ['latin'] });
import { ThemeProvider } from '@/components/providers/theme-provider';
import { cn } from '@/lib/utils';

export const metadata: Metadata = {
  title: 'UMassenger',
  description: 'UMass CS520 Project',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" suppressContentEditableWarning>
      <body className={cn(font.className, 'bg-while dark:bg-[#282a30]')}>
        <ThemeProvider
          attribute="class"
          defaultTheme="dark"
          enableSystem={false}
          storageKey="UMassenger"
        >
          <Navbar />
          {children}
        </ThemeProvider>
      </body>
    </html>
  );
}