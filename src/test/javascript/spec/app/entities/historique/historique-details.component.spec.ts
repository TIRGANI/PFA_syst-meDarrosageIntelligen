/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import HistoriqueDetailComponent from '@/entities/historique/historique-details.vue';
import HistoriqueClass from '@/entities/historique/historique-details.component';
import HistoriqueService from '@/entities/historique/historique.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Historique Management Detail Component', () => {
    let wrapper: Wrapper<HistoriqueClass>;
    let comp: HistoriqueClass;
    let historiqueServiceStub: SinonStubbedInstance<HistoriqueService>;

    beforeEach(() => {
      historiqueServiceStub = sinon.createStubInstance<HistoriqueService>(HistoriqueService);

      wrapper = shallowMount<HistoriqueClass>(HistoriqueDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { historiqueService: () => historiqueServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundHistorique = { id: 123 };
        historiqueServiceStub.find.resolves(foundHistorique);

        // WHEN
        comp.retrieveHistorique(123);
        await comp.$nextTick();

        // THEN
        expect(comp.historique).toBe(foundHistorique);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundHistorique = { id: 123 };
        historiqueServiceStub.find.resolves(foundHistorique);

        // WHEN
        comp.beforeRouteEnter({ params: { historiqueId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.historique).toBe(foundHistorique);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
