import { IPlante } from '@/shared/model/plante.model';
import { IParcelle } from '@/shared/model/parcelle.model';

export interface IPlantage {
  id?: number;
  date?: string | null;
  nbrPlate?: number | null;
  plantes?: IPlante[] | null;
  parcelles?: IParcelle[] | null;
}

export class Plantage implements IPlantage {
  constructor(
    public id?: number,
    public date?: string | null,
    public nbrPlate?: number | null,
    public plantes?: IPlante[] | null,
    public parcelles?: IParcelle[] | null
  ) {}
}
