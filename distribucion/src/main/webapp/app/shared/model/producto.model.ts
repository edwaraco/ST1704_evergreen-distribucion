import dayjs from 'dayjs';

export interface IProducto {
  id?: number;
  identificador?: string;
  nombre?: string;
  descripcion?: string | null;
  fechaElaboracion?: dayjs.Dayjs;
  lote?: string | null;
  cantidad?: number;
  unidadMedida?: string | null;
}

export const defaultValue: Readonly<IProducto> = {};
