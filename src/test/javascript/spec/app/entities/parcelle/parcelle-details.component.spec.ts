/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ParcelleDetailComponent from '@/entities/parcelle/parcelle-details.vue';
import ParcelleClass from '@/entities/parcelle/parcelle-details.component';
import ParcelleService from '@/entities/parcelle/parcelle.service';
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
  describe('Parcelle Management Detail Component', () => {
    let wrapper: Wrapper<ParcelleClass>;
    let comp: ParcelleClass;
    let parcelleServiceStub: SinonStubbedInstance<ParcelleService>;

    beforeEach(() => {
      parcelleServiceStub = sinon.createStubInstance<ParcelleService>(ParcelleService);

      wrapper = shallowMount<ParcelleClass>(ParcelleDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { parcelleService: () => parcelleServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundParcelle = { id: 123 };
        parcelleServiceStub.find.resolves(foundParcelle);

        // WHEN
        comp.retrieveParcelle(123);
        await comp.$nextTick();

        // THEN
        expect(comp.parcelle).toBe(foundParcelle);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundParcelle = { id: 123 };
        parcelleServiceStub.find.resolves(foundParcelle);

        // WHEN
        comp.beforeRouteEnter({ params: { parcelleId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.parcelle).toBe(foundParcelle);
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
