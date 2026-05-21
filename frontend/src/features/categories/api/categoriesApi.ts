import client from '../../../api/client';
import type { Category, CategoryUpsertPayload } from '../types/category';

export const categoriesApi = {
  list: async (): Promise<Category[]> => {
    const response = await client.get<Category[]>('/api/categories');
    return response.data;
  },

  show: async (id: string): Promise<Category> => {
    const response = await client.get<Category>(`/api/categories/${id}`);
    return response.data;
  },

  create: async (payload: CategoryUpsertPayload): Promise<Category> => {
    const response = await client.post<Category>('/api/categories', payload);
    return response.data;
  },

  update: async (id: string, payload: CategoryUpsertPayload): Promise<Category> => {
    const response = await client.put<Category>(`/api/categories/${id}`, payload);
    return response.data;
  },

  remove: async (id: string): Promise<void> => {
    await client.delete(`/api/categories/${id}`);
  },
};
