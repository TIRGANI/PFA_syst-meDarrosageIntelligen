import { ICapteur } from '@/shared/model/capteur.model';
import { IBoitier } from '@/shared/model/boitier.model';

export interface IBrache {
  id?: number;
  branche?: number;
  capteurs?: ICapteur[] | null;
  boitiers?: IBoitier[] | null;
}

export class Brache implements IBrache {
  constructor(public id?: number, public branche?: number, public capteurs?: ICapteur[] | null, public boitiers?: IBoitier[] | null) {}
}
