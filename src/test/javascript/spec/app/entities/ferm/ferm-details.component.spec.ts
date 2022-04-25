/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import FermDetailComponent from '@/entities/ferm/ferm-details.vue';
import FermClass from '@/entities/ferm/ferm-details.component';
import FermService from '@/entities/ferm/ferm.service';
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
  describe('Ferm Management Detail Component', () => {
    let wrapper: Wrapper<FermClass>;
    let comp: FermClass;
    let fermServiceStub: SinonStubbedInstance<FermService>;

    beforeEach(() => {
      fermServiceStub = sinon.createStubInstance<FermService>(FermService);

      wrapper = shallowMount<FermClass>(FermDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { fermService: () => fermServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundFerm = { id: 123 };
        fermServiceStub.find.resolves(foundFerm);

        // WHEN
        comp.retrieveFerm(123);
        await comp.$nextTick();

        // THEN
        expect(comp.ferm).toBe(foundFerm);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFerm = { id: 123 };
        fermServiceStub.find.resolves(foundFerm);

        // WHEN
        comp.beforeRouteEnter({ params: { fermId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.ferm).toBe(foundFerm);
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
