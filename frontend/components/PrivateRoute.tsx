'use client';

import { useRouter, usePathname } from 'next/navigation';
import { ReactNode, useEffect } from 'react';
import { useUserService } from '@/utils';

interface PrivateRouteProps {
  children: ReactNode;
}

const PrivateRoute = ({ children }: PrivateRouteProps) => {
  const router = useRouter();
  const pathname = usePathname();
  const userService = useUserService();

  useEffect(() => {
    const checkUserAndRedirect = async () => {
      const currentUser = await userService.getCurrent();
      if (!currentUser) {
        const returnUrl = encodeURIComponent(pathname);
        // should router.push, redirect should in useEffect else it will be recognized to server rendering
        router.push(`/login?returnUrl=${returnUrl}`);
      }
    };

    checkUserAndRedirect();
  }, []);

  return children;
};

export default PrivateRoute;
