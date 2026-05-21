export type Category = {
  id: string;
  name: string;
  type: number;
  description: string | null;
  createdAt: string;
  updatedAt: string | null;
};

export type CategoryUpsertPayload = {
  name: string;
  type: number;
  description: string;
};
