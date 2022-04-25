<template>
  <div>
    <b-dropdown-item to="/historique">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.historique')">Historique</span>
    </b-dropdown-item>
    <b-dropdown-item to="/type-sol">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.typeSol')">Type Sol</span>
    </b-dropdown-item>
    <b-dropdown-item to="/parcelle">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.parcelle')">Parcelle</span>
    </b-dropdown-item>
    <b-dropdown-item to="/ferm">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.ferm')">Ferm</span>
    </b-dropdown-item>
    <b-dropdown-item to="/alerte">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.alerte')">Alerte</span>
    </b-dropdown-item>
    <b-dropdown-item to="/affectation">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.affectation')">Affectation</span>
    </b-dropdown-item>
    <b-dropdown-item to="/grandeur">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.grandeur')">Grandeur</span>
    </b-dropdown-item>
    <b-dropdown-item to="/plantage">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.plantage')">Plantage</span>
    </b-dropdown-item>
    <b-dropdown-item to="/plante">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.plante')">Plante</span>
    </b-dropdown-item>
    <b-dropdown-item to="/type-plant">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.typePlant')">Type Plant</span>
    </b-dropdown-item>
    <b-dropdown-item to="/boitier">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.boitier')">Boitier</span>
    </b-dropdown-item>
    <b-dropdown-item to="/brache">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.brache')">Brache</span>
    </b-dropdown-item>
    <b-dropdown-item to="/capteur">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.capteur')">Capteur</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
