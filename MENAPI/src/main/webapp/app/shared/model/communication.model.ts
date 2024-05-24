import { IUnite } from 'app/shared/model/unite.model';

export interface ICommunication {
  id?: number;
  libelle?: string;
  fe?: number | null;
  feUnite?: string | null;
  unite?: IUnite | null;
}

export const defaultValue: Readonly<ICommunication> = {};
