import client from '../../../api/client';
import type { DashboardSummary } from '../types/dashboard';

export const dashboardApi = {
  summary: async (): Promise<DashboardSummary> => {
    const response = await client.get<DashboardSummary>('/api/dashboard');
    return response.data;
  },
};
