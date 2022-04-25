import { IBoitier } from '@/shared/model/boitier.model';
import { IParcelle } from '@/shared/model/parcelle.model';

export interface IAffectation {
  id?: number;
  dateDebut?: string | null;
  dateFin?: string | null;
  boitier?: IBoitier | null;
  parcelle?: IParcelle | null;
}

export class Affectation implements IAffectation {
  constructor(
    public id?: number,
    public dateDebut?: string | null,
    public dateFin?: string | null,
    public boitier?: IBoitier | null,
    public parcelle?: IParcelle | null
  ) {}
}
