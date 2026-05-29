export type DashboardAlert = {
  id: string;
  title: string;
  message: string;
  date: string;
};

export type DashboardAccount = {
  id: string;
  name: string;
  subtitle: string;
  balance: number;
  accountType: number;
  progressPercentage: number | null;
};

export type DashboardLiquidityPoint = {
  date: string;
  projectedBalance: number;
};

export type DashboardLiquidity = {
  message: string;
  points: DashboardLiquidityPoint[];
};

export type DashboardTransaction = {
  id: string;
  title: string;
  subtitle: string;
  amount: number;
  positive: boolean;
  date: string;
};

export type DashboardSummary = {
  userId: string;
  greetingName: string;
  totalBalance: number;
  currency: string;
  monthlyBalanceVariationPercent: number;
  nextAlert: DashboardAlert | null;
  accounts: DashboardAccount[];
  liquidity: DashboardLiquidity;
  recentTransactions: DashboardTransaction[];
};
