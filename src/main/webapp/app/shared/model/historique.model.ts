import { IParcelle } from '@/shared/model/parcelle.model';

export interface IHistorique {
  id?: number;
  dateArosage?: string | null;
  qttEau?: number | null;
  parcelle?: IParcelle | null;
}

export class Historique implements IHistorique {
  constructor(public id?: number, public dateArosage?: string | null, public qttEau?: number | null, public parcelle?: IParcelle | null) {}
}
