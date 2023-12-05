import { ReactNode } from 'react';
import PrivateRoute from '@/components/PrivateRoute';

export default function PrivateLayout({ children }: { children: ReactNode }) {
  return <PrivateRoute>{children}</PrivateRoute>;
}
