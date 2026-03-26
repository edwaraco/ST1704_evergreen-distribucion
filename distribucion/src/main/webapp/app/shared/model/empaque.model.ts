import dayjs from 'dayjs';
import { IPedido } from 'app/shared/model/pedido.model';
import { TipoEmpaque } from 'app/shared/model/enumerations/tipo-empaque.model';
import { TamanioEmpaque } from 'app/shared/model/enumerations/tamanio-empaque.model';

export interface IEmpaque {
  id?: number;
  identificador?: string;
  tipo?: keyof typeof TipoEmpaque;
  tamanio?: keyof typeof TamanioEmpaque;
  cantidad?: number;
  tiempoMinutos?: number;
  fechaRealizacion?: dayjs.Dayjs | null;
  responsable?: string | null;
  observaciones?: string | null;
  pedido?: IPedido;
}

export const defaultValue: Readonly<IEmpaque> = {};
