/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import HistoriqueComponent from '@/entities/historique/historique.vue';
import HistoriqueClass from '@/entities/historique/historique.component';
import HistoriqueService from '@/entities/historique/historique.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Historique Management Component', () => {
    let wrapper: Wrapper<HistoriqueClass>;
    let comp: HistoriqueClass;
    let historiqueServiceStub: SinonStubbedInstance<HistoriqueService>;

    beforeEach(() => {
      historiqueServiceStub = sinon.createStubInstance<HistoriqueService>(HistoriqueService);
      historiqueServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<HistoriqueClass>(HistoriqueComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          historiqueService: () => historiqueServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      historiqueServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllHistoriques();
      await comp.$nextTick();

      // THEN
      expect(historiqueServiceStub.retrieve.called).toBeTruthy();
      expect(comp.historiques[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      historiqueServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(historiqueServiceStub.retrieve.callCount).toEqual(1);

      comp.removeHistorique();
      await comp.$nextTick();

      // THEN
      expect(historiqueServiceStub.delete.called).toBeTruthy();
      expect(historiqueServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
