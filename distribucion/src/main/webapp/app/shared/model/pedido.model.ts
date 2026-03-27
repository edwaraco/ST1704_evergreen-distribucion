import dayjs from 'dayjs';
import { ICliente } from 'app/shared/model/cliente.model';
import { IProducto } from 'app/shared/model/producto.model';
import { ICanalComercializacion } from 'app/shared/model/canal-comercializacion.model';
import { ITransporte } from 'app/shared/model/transporte.model';

export interface IPedido {
  id?: number;
  identificador?: string;
  fechaEntrada?: dayjs.Dayjs;
  fechaSalida?: dayjs.Dayjs | null;
  estado?: string;
  cliente?: ICliente;
  productos?: IProducto[] | null;
  canalComercializacion?: ICanalComercializacion | null;
  transporte?: ITransporte | null;
}

export const defaultValue: Readonly<IPedido> = {};
