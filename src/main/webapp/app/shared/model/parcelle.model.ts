import { IHistorique } from '@/shared/model/historique.model';
import { IGrandeur } from '@/shared/model/grandeur.model';
import { IAffectation } from '@/shared/model/affectation.model';
import { IFerm } from '@/shared/model/ferm.model';
import { IPlantage } from '@/shared/model/plantage.model';
import { ITypeSol } from '@/shared/model/type-sol.model';
import { IAlerte } from '@/shared/model/alerte.model';

export interface IParcelle {
  id?: number;
  surface?: number | null;
  photo?: string | null;
  historiques?: IHistorique[] | null;
  grandeurs?: IGrandeur[] | null;
  affectations?: IAffectation[] | null;
  ferms?: IFerm[] | null;
  plantages?: IPlantage[] | null;
  typeSol?: ITypeSol | null;
  alertes?: IAlerte[] | null;
}

export class Parcelle implements IParcelle {
  constructor(
    public id?: number,
    public surface?: number | null,
    public photo?: string | null,
    public historiques?: IHistorique[] | null,
    public grandeurs?: IGrandeur[] | null,
    public affectations?: IAffectation[] | null,
    public ferms?: IFerm[] | null,
    public plantages?: IPlantage[] | null,
    public typeSol?: ITypeSol | null,
    public alertes?: IAlerte[] | null
  ) {}
}
