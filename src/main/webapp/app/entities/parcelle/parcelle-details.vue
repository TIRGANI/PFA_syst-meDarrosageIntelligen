<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="parcelle">
        <h2 class="jh-entity-heading" data-cy="parcelleDetailsHeading">
          <span v-text="$t('pfaApp.parcelle.detail.title')">Parcelle</span> {{ parcelle.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('pfaApp.parcelle.surface')">Surface</span>
          </dt>
          <dd>
            <span>{{ parcelle.surface }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.parcelle.photo')">Photo</span>
          </dt>
          <dd>
            <span>{{ parcelle.photo }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.parcelle.ferm')">Ferm</span>
          </dt>
          <dd>
            <span v-for="(ferm, i) in parcelle.ferms" :key="ferm.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'FermView', params: { fermId: ferm.id } }">{{ ferm.id }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.parcelle.plantage')">Plantage</span>
          </dt>
          <dd>
            <span v-for="(plantage, i) in parcelle.plantages" :key="plantage.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'PlantageView', params: { plantageId: plantage.id } }">{{ plantage.id }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.parcelle.typeSol')">Type Sol</span>
          </dt>
          <dd>
            <div v-if="parcelle.typeSol">
              <router-link :to="{ name: 'TypeSolView', params: { typeSolId: parcelle.typeSol.id } }">{{ parcelle.typeSol.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="parcelle.id" :to="{ name: 'ParcelleEdit', params: { parcelleId: parcelle.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./parcelle-details.component.ts"></script>
