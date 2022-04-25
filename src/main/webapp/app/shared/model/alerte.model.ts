import { IParcelle } from '@/shared/model/parcelle.model';
import { IBoitier } from '@/shared/model/boitier.model';

export interface IAlerte {
  id?: number;
  humidite?: number | null;
  temperature?: number | null;
  luminosite?: number | null;
  parcelle?: IParcelle | null;
  boitier?: IBoitier | null;
}

export class Alerte implements IAlerte {
  constructor(
    public id?: number,
    public humidite?: number | null,
    public temperature?: number | null,
    public luminosite?: number | null,
    public parcelle?: IParcelle | null,
    public boitier?: IBoitier | null
  ) {}
}
