#!/usr/bin/env zx

import _ from "lodash";
import fs from "fs-extra";

async function getRepoFile(name, type = "default") {
    try {
        return await fs.readJSONSync(`../config/repo/${name}.${type}.json`);
    } catch (_) {
        return {};
    }
}

async function getSettings() {
    const defaultSettings = await getRepoFile("settings");
    const overrideSettings = await getRepoFile("settings", "override");
    return _.merge(defaultSettings, overrideSettings);
}

async function run() {
    if (await fs.existsSync("../../.gitmodules")) {
        echo`.gitmodules installed, updating modules`;
        $`git submodule update --init --recursive`;
        return;
    }
    const settings = await getSettings();
    const modules = settings["git-submodules"];
    echo`Adding git submodules`;
    modules.forEach(
        (module) => $`git submodule add ${module.url} ../../${module.path}`
    );
}

await run();
