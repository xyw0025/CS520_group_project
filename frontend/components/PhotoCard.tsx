import Image from 'next/image';
import { CrossCircledIcon, PlusCircledIcon } from '@radix-ui/react-icons';
import { cn } from '@/lib/utils';
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Photo } from '../data';

interface PhotoProps extends React.HTMLAttributes<HTMLDivElement> {
  photo?: Photo;
  aspectRatio?: 'portrait' | 'square';
  width?: number;
  height?: number;
  onRemove?: (photoName: string) => void;
  isPlaceholder?: boolean;
  onAdd?: () => void;
}

export default function PhotoCard({
  photo,
  aspectRatio = 'portrait',
  width,
  height,
  className,
  onRemove,
  isPlaceholder = false,
  onAdd,
  ...props
}: PhotoProps) {
  const handleRemoveClick = () => {
    if (onRemove && photo) {
      onRemove(photo.name);
    }
  };
  const handleAddClick = () => {
    if (onAdd) {
      onAdd();
    }
  };
  if (isPlaceholder) {
    return (
      <div className={cn('space-y-3', className)} {...props}>
        <Card>
          <PlusCircledIcon
            className="text-green-700 scale-150 cursor-pointer hover:scale-[2.0]"
            onClick={handleAddClick}
          />
        </Card>
      </div>
    );
  }

  return (
    <div className={cn('space-y-3', className)} {...props}>
      <Card>
        <CrossCircledIcon
          className="text-red-700 scale-150 cursor-pointer hover:scale-[2.0]"
          onClick={handleRemoveClick}
        />

        {photo && (
          <CardContent>
            <div className="overflow-hidden rounded-md">
              <Image
                src={photo.cover}
                alt={photo.name}
                width={width}
                height={height}
                className={cn(
                  'h-auto w-auto object-cover transition-all hover:scale-105',
                  aspectRatio === 'portrait' ? 'aspect-[3/4]' : 'aspect-square'
                )}
              />
            </div>
          </CardContent>
        )}
      </Card>
    </div>
  );
}
