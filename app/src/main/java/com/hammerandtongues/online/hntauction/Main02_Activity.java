package com.hammerandtongues.online.hntauction;

/**
 * Created by Ruvimbo on 2/2/2018.
 */
public class Main02_Activity{
       // extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.gc();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        if (tb != null) {
            tb.setVisibility(View.GONE);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Categories"));
        tabLayout.addTab(tabLayout.newTab().setText("Stores"));
        tabLayout.addTab(tabLayout.newTab().setText("Promotions"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final PagerAdapter adapter = new PagerAdapter01(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        final LinearLayout btnhome, btncategs, btnsearch, btncart, btnprofile;

        btnhome = (LinearLayout) findViewById(R.id.btn_home);
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main02_Activity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btncategs = (LinearLayout) findViewById(R.id.btn_Categories);
        btncategs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main02_Activity.this, Main02_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btncart = (LinearLayout) findViewById(R.id.btn_Cart);
        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main02_Activity.this, Cart.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnprofile = (LinearLayout) findViewById(R.id.btn_Profile);
        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main02_Activity.this, UserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnsearch = (LinearLayout) findViewById(R.id.btn_Search01);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main02_Activity.this, Search.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        ImageView imgsearch, imgcategories, imghome, imgcart, imgprofile;
        imgsearch = (ImageView) findViewById(R.id.imgsearch01);
        imgsearch.setImageResource(R.drawable.search);

        imghome = (ImageView) findViewById(R.id.imghome);
        imghome.setImageResource(R.drawable.home);

        imgcategories = (ImageView) findViewById(R.id.imgcategs);
        imgcategories.setImageResource(R.drawable.categoryselected);

        imgcart = (ImageView) findViewById(R.id.imgcart);
        imgcart.setImageResource(R.drawable.cart02);

        imgprofile = (ImageView) findViewById(R.id.imgprofile);
        imgprofile.setImageResource(R.drawable.profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
        @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_search) {
            Intent intent = new Intent(Main02_Activity.this, Search.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        if (id == R.id.cart_item) {
            Intent intent = new Intent(Main02_Activity.this, Cart.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.myaccount) {
            Intent intent = new Intent(Main02_Activity.this, UserActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.cart) {
            Intent intent = new Intent(Main02_Activity.this, Cart.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.mytransactions) {
            Intent intent = new Intent(Main02_Activity.this, TransactionHistory.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }/* else if (id == R.id.favourites) {
            Intent intent = new Intent(Main02_Activity.this, UserActivity.class);
            startActivity(intent);


        } else if (id == R.id.messages) {
            Intent intent = new Intent(Main02_Activity.this, UserActivity.class);
            startActivity(intent);

        } */
    /*else if (id == R.id.about_us) {
            Intent intent = new Intent(Main02_Activity.this, AboutUs.class);
            startActivity(intent);

        } else if (id == R.id.contact_us) {
            Intent intent = new Intent(Main02_Activity.this, ContactUs.class);
            startActivity(intent);

        } else if (id == R.id.terms) {
            Intent intent = new Intent(Main02_Activity.this, TermsAndConditions.class);
            startActivity(intent);
        }
        else if (id == R.id.faqs) {
            Intent intent = new Intent(Main02_Activity.this, Faqs.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }
*/
}