import dayjs from 'dayjs';
import { IPedido } from 'app/shared/model/pedido.model';

export interface ISeparacion {
  id?: number;
  identificador?: string;
  lote?: string;
  cantidad?: number;
  fechaRealizacion?: dayjs.Dayjs | null;
  ubicacion?: string | null;
  responsable?: string | null;
  observaciones?: string | null;
  pedido?: IPedido;
}

export const defaultValue: Readonly<ISeparacion> = {};
