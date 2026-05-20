import client from '../../../api/client';
import type { RegisterUserRequest, RegisterUserResponse } from '../types/register';

export const registerUser = async (
  payload: RegisterUserRequest,
): Promise<RegisterUserResponse> => {
  const response = await client.post<RegisterUserResponse>('/registries/users', payload);
  return response.data;
};
