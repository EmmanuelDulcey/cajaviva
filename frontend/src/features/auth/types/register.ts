export type RegisterUserRequest = {
  name: string;
  lastName: string;
  email: string;
  password: string;
};

export type RegisterUserResponse = {
  id: string;
  name: string;
  lastName: string;
  email: string;
};
