import dayjs from 'dayjs';
import { IPedido } from 'app/shared/model/pedido.model';

export interface IProducto {
  id?: number;
  identificador?: string;
  nombre?: string;
  descripcion?: string | null;
  fechaElaboracion?: dayjs.Dayjs;
  lote?: string | null;
  cantidad?: number;
  unidadMedida?: string | null;
  pedidos?: IPedido[] | null;
}

export const defaultValue: Readonly<IProducto> = {};
