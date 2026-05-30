import client from '../../../api/client';
import type { LiquidityProjection, LiquidityProjectionRequest } from '../types/projection';

export const projectionsApi = {
  list: async (): Promise<LiquidityProjection[]> => {
    const response = await client.get<LiquidityProjection[]>('/api/liquidity-projections');
    return response.data;
  },

  show: async (id: string): Promise<LiquidityProjection> => {
    const response = await client.get<LiquidityProjection>(`/api/liquidity-projections/${id}`);
    return response.data;
  },

  findByAccount: async (accountId: string): Promise<LiquidityProjection[]> => {
    const response = await client.get<LiquidityProjection[]>(`/api/liquidity-projections/account/${accountId}`);
    return response.data;
  },

  calculate: async (payload: LiquidityProjectionRequest): Promise<LiquidityProjection[]> => {
    const response = await client.post<LiquidityProjection[]>('/api/liquidity-projections/calculate', payload);
    return response.data;
  },

  create: async (projection: Partial<LiquidityProjection>): Promise<LiquidityProjection> => {
    const response = await client.post<LiquidityProjection>('/api/liquidity-projections', projection);
    return response.data;
  },

  update: async (id: string, projection: Partial<LiquidityProjection>): Promise<LiquidityProjection> => {
    const response = await client.put<LiquidityProjection>(`/api/liquidity-projections/${id}`, projection);
    return response.data;
  },

  remove: async (id: string): Promise<void> => {
    await client.delete(`/api/liquidity-projections/${id}`);
  },
};
