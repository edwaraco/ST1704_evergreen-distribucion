import dayjs from 'dayjs';
import { TipoTransporte } from 'app/shared/model/enumerations/tipo-transporte.model';

export interface ITransporte {
  id?: number;
  identificador?: string;
  tipoTransporte?: keyof typeof TipoTransporte;
  matricula?: string | null;
  capacidadKg?: number | null;
  capacidadM3?: number | null;
  estado?: string;
  fechaAsignacion?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ITransporte> = {};
