export interface ICanalComercializacion {
  id?: number;
  identificador?: string;
  nombre?: string;
  descripcion?: string | null;
  activo?: boolean;
}

export const defaultValue: Readonly<ICanalComercializacion> = {
  activo: false,
};
