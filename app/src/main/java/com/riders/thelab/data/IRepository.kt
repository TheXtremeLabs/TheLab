package com.riders.thelab.data

import com.riders.thelab.data.local.IDb
import com.riders.thelab.data.remote.IApi

interface IRepository : IDb, IApi {
}