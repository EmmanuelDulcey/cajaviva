export type LiquidityProjection = {
  id: string;
  calculationDate: string;
  projectedBalance: number;
  projectionDate: string;
  createdAt: string;
  updatedAt: string | null;
  account: {
    id: string;
    name: string;
    balance: number;
  };
};

export type LiquidityProjectionRequest = {
  accountId: string;
  startDate: string;
  endDate: string;
};
