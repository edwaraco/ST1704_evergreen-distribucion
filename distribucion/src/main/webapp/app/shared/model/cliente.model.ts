export interface ICliente {
  id?: number;
  identificador?: string;
  nombre?: string;
  email?: string | null;
  telefono?: string | null;
  direccion?: string | null;
}

export const defaultValue: Readonly<ICliente> = {};
