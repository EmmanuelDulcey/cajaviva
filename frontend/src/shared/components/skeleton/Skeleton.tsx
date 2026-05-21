import styles from './Skeleton.module.css';

type SkeletonProps = {
  className?: string;
};

export function Skeleton({ className = '' }: SkeletonProps) {
  return <div className={`${styles.skeleton} ${className}`.trim()} aria-hidden="true" />;
}
